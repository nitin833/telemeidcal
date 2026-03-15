
const Chat = require("../models/chat");
const Messages = require("../models/message")

const IO = require("../config/ioconfig");

const createChat  = async (Appoid)=>{

let c =await new Chat({appointmentId:Appoid});
return c.save();

}


const activateChat =async (Appoid)=>{

let c = await Chat.findById({appointmentId:Appoid});
    if(c==null){
        const error = new Error();
        error.message = "cannot get chat with appointment";
        throw error;
    }
    c.active=true;
    return c.save();
}

const deactivateChat = async (Appoid)=>{

    let c = await Chat.findById({appointmentId:Appoid});
    if(c==null){
        const error = new Error();
        error.message = "cannot get chat with appointment";
        throw error;
    }
    c.active=false;
    return c.save();

}


const sendMessage = async (Appoid,reciver,sender,message)=>{

    
    let c = await Chat.findById({appointmentId:Appoid});
    if(c==null){
        const error = new Error();
        error.message = "cannot get chat with appointment";
        throw error;
    }
    let m = new Message({senderId:sender,text:message});
    c.messages.push(m);
    c.save().then((data)=>{

       const io = IO.getIo();
        io.to(IO.users[reciver]).emit("message",{message:message,senderId:sender});

    }).catch(err=>{
           const error = new Error();
        error.message = "error in updating db";
        throw error;
    })



}


