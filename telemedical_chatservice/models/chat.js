const mongoose = require("mongoose")

const Messages = require("./message")

const ChatSchema = new mongoose.Schema({


appointmentId:{
    type:String,
    required:true
},
active:{
    type:Boolean
},
messages:[
    {
        type:mongoose.Schema.Types.ObjectId,
        ref:"Messages"
    }
]
})

module.exports = mongoose.model("Chat",ChatSchema);