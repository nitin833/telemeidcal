const mongoose = require("mongoose")


const messageSchema = mongoose.Schema({


    senderId:{
    type:String,
    required:true
    },
    date:{
        type:Date,
        required:true
    }
})


module.exports = mongoose.model("Messages",messageSchema);