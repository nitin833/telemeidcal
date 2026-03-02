const mongoose = require("mongoose")

const connectDb = async () =>{
    try{
        const conn = await mongoose.connect(process.env.MONGO_URI);
        console.log("mongoose connected");
    }
    catch(err){
        console.log("error in connecting with mongoose");
    }
}

module.exports=connectDb;