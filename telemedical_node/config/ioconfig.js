


let io=null;


const IO = {

    users:{},

    init:(server)=>{

        io = require("socket.io")(server, {
            cors: {
                origin: "*",
                methods: ["GET", "POST"]
            }
        });
        io.on("connection", (socket) => {

    const userId = socket.handshake.auth.userId;
    IO.users[userId] = socket.id;

    /*
            use this in frontend      
            const socket = io("http://localhost:3000", {
            auth: {
                userId: "12345"
            }
});
    
    */

});
    },
    getIo:()=>{
        if(io==null){
            throw new Error("connection not initiated");
        }
        return io;

    }

}


module.exports = IO;
