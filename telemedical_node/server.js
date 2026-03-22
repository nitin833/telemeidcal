const express = require('express');
const multer = require('multer');
const app = express();

const path = require('path');


const chatController = require("./controllers/chatController");
const IO = require("./config/ioconfig");

import {Hospital} from "./models/hospitalModel";

const fileremove = (req,res,next)=>{
    const filename = req.params.filename;
    const filePath = `uploads/${filename}`;
    path.unlink(filePath, (err) => {
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
app.use("/chat",chatController);


app.post("/upload", upload.single('file'), (req, res) => {
    if (!req.file) {
        return res.status(400).json({ error: 'No file uploaded' });
    }
    res.json({ message: 'File uploaded successfully', file: req.file ,path: req.file.path});
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
    path.unlink(filePath, (err) => {
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

const server = app.listen(3000,()=>{
    console.log("server is running on port 3000");
    IO.init(server);
});