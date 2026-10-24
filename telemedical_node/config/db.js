const mongoose = require("mongoose");
const { MongoMemoryServer } = require("mongodb-memory-server");

let mongoServer;

const connectDb = async () => {
    mongoServer = await MongoMemoryServer.create();
    await mongoose.connect(mongoServer.getUri(), { dbName: "telemedical" });
    console.log("mongoose connected to the in-memory MongoDB server");
};

const disconnectDb = async () => {
    await mongoose.disconnect();
    if (mongoServer) {
        await mongoServer.stop();
        mongoServer = undefined;
    }
};

module.exports = { connectDb, disconnectDb };
