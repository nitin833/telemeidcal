const express = require('express');
const app = express();


const chatController = require("./controllers/chatController");
const IO = require("./config/ioconfig");

import {Hospital} from "./models/hospitalModel";

app.use(express.json());
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

const server = app.listen(3000,()=>{
    console.log("server is running on port 3000");
    IO.init(server);
});