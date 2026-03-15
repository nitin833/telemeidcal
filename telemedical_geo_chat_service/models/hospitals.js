const mongoose = require("mongoose")

const hospitalSchema = new mongoose.Schema({


    _id:{
        type:String
    },
    location: {
    type: {
      type: String,
      enum: ["Point"],   
      required: true
    },
    coordinates: {
      type: [Number],   
      required: true
    }
  }


});




hospitalSchema.index({location:"2dsphere"})
module.exports = mongoose.model(hospitalSchema,"hospital")