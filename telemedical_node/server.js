require('dotenv').config();
const express = require('express');
const cors = require('cors');
const multer = require('multer');
const app = express();
app.use(cors());
const { connectDb, disconnectDb } = require("./config/db");

const path = require('path');
const jwt = require('jsonwebtoken');

const chatController = require("./controllers/chatController");
const IO = require("./config/ioconfig");
const Hospital = require("./models/hospitals");
const fs = require('fs');


const fileremove = (req,res,next)=>{
    const filename = req.params.filename;
    const filePath = `uploads/${filename}`;
    fs.unlink(filePath, (err) => {
        if (err) {
            return res.status(404).json({ error: 'File not found' });
        }
        next();
    });
}


const storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'uploads/');
    },
    filename: function (req, file, cb) {
      cb(null,file.originalname);
    }
  });


const upload = multer({ storage: storage });

app.use(express.json());


app.use(async (req,res,next)=>{

    let token = req.headers.authorization;
    if(!token){
        return res.status(401).json({error:"Unauthorized"});
    }
    token = token.split(" ")[1];
    try{
        const decoded = jwt.verify(token, process.env.JWT_SECRET);
        req.authorities = decoded.authorities || [];
        next();
    }catch(err){
        return res.status(401).json({error:"Unauthorized"});
    }



})


app.use(express.static('uploads'));
app.use("/chat",chatController);

app.get("/nearestHospitals/:distance",async (req,res)=>{

let {latitude,longitude} = req.query;
let distance = req.params.distance;


let list = await Hospital.find({
    location: {
        $near: {
            $geometry: {
                type: "Point",
                coordinates: [parseFloat(longitude), parseFloat(latitude)]
            },
            $maxDistance: parseFloat(distance)
        }
    }});

    res.json(list);
})


app.use((req,res,next)=>{
    const arr = req.authorities;
    if(arr.includes("ROLE_ADMIN") || arr.includes("ROLE_OWNER") || arr.includes("ROLE_DOCTOR")){
        next();
    }else{
        return res.status(403).json({error:"Forbidden"});
    }
});


app.post("/upload", upload.single('file'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ error: 'No file uploaded' });
    }
    res.json({ message: 'File uploaded successfully', file: req.file ,path: req.file.path});
});

app.use((req,res,next)=>{
    const arr = req.authorities;
    if(arr.includes("ROLE_ADMIN") || arr.includes("ROLE_OWNER")){
        next();
    }else{
        return res.status(403).json({error:"Forbidden"});
    }
});


app.get("/files/:filename", (req, res) => {
    const filename = req.params.filename;
    const filePath = `uploads/${filename}`;
    res.sendFile(filePath, { root: __dirname }, (err) => {
        if (err) {
            res.status(404).json({ error: 'File not found' });
        }
    });
});

app.delete("/files/:filename", (req, res) => {
    const filename = req.params.filename;
    const filePath = `uploads/${filename}`;
    fs.unlink(filePath, (err) => {
        if (err) {
            return res.status(404).json({ error: 'File not found' });
        }
        res.json({ message: 'File deleted successfully' });
    });
});

app.put("/files/:filename",fileremove ,upload.single('file'), (req, res) => {
  
       const filename = req.params.filename;
    const filePath = `uploads/${filename}`;
    res.sendFile(filePath, { root: __dirname }, (err) => {
        if (err) {
            res.status(404).json({ error: 'File not found' });
        }
    });
});






app.post("/addHospital",async (req,res)=>{
    let {name,latitude,longitude} = req.body;

    let hospital = new Hospital({
        name,
        location: {
            type: "Point",
            coordinates: [parseFloat(longitude), parseFloat(latitude)]
        }
    });
    await hospital.save();
    res.json(hospital);
});


app.delete("/deleteHospital/:id",async (req,res)=>{
    let id = req.params.id;
    await Hospital.findByIdAndDelete(id);
    res.json({message:"Hospital deleted successfully"});
});

const startServer = async () => {
    await connectDb();

    const server = app.listen(3000, () => {
        console.log("server is running on port 3000");
        IO.init(server);
    });

    const shutdown = async () => {
        server.close(async () => {
            await disconnectDb();
            process.exit(0);
        });
    };

    process.once("SIGINT", shutdown);
    process.once("SIGTERM", shutdown);
};

startServer().catch((error) => {
    console.error("Failed to start the server", error);
    process.exit(1);
});
