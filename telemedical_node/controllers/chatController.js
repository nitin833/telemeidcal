
const express = require('express');
const router = express.Router();
const chatService = require("../services/chatService");

router.post("/createChat",async (req,res)=>{
    const {appointmentId} = req.body;
    try{
        const chat = await chatService.createChat(appointmentId);
        res.status(200).json({message:"chat created",chat:chat});
    }catch(err){
        res.status(500).json({message:err.message});
    }
});

router.post("/activateChat",async (req,res)=>{
    const {appointmentId} = req.body;
    try{
        const chat = await chatService.activateChat(appointmentId);
        res.status(200).json({message:"chat activated",chat:chat});
    }
    catch(err){
        res.status(500).json({message:err.message});
    }

});

router.post("/deactivateChat",async (req,res)=>{
    const {appointmentId} = req.body;
    try{
        const chat = await chatService.deactivateChat(appointmentId);
        res.status(200).json({message:"chat deactivated",chat:chat});
    }
    catch(err){
        res.status(500).json({message:err.message});
    }
});


router.post("/sendMessage",async (req,res)=>{
    const {appointmentId,reciver,sender,message} = req.body;
    try{
        await chatService.sendMessage(appointmentId,reciver,sender,message);
        res.status(200).json({message:"message sent"});
    }
    catch(err){
        res.status(500).json({message:err.message});
    }
});

module.exports = router;

