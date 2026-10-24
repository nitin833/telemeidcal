import React, { useState, useEffect, useRef } from 'react';
import { io } from 'socket.io-client';
import { useSelector } from 'react-redux';
import { selectAuth } from '../store/authSlice';

// A shared socket instance can be kept outside or managed via Context. 
// For simplicity, we'll initialize it in the component.

export default function Chat({ appointmentId, receiverId }) {
  const { user, token } = useSelector(selectAuth);
  const [messages, setMessages] = useState([]);
  const [newMessage, setNewMessage] = useState('');
  const [socket, setSocket] = useState(null);
  const [uploading, setUploading] = useState(false);
  const messagesEndRef = useRef(null);

  // Scroll to bottom when messages update
  useEffect(() => {
    messagesEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  }, [messages]);

  useEffect(() => {
    if (!user?.id) return;

    // Connect to Socket.IO Node.js server
    const newSocket = io('http://localhost:3000', {
      auth: { userId: user.id }
    });

    setSocket(newSocket);

    newSocket.on('message', (data) => {
      // data: { message, senderId }
      setMessages(prev => [...prev, { text: data.message, senderId: data.senderId }]);
    });

    // Attempt to create chat on mount (safe to fail if already exists)
    fetch('http://localhost:3000/chat/createChat', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
      body: JSON.stringify({ appointmentId })
    }).catch(console.error);

    return () => newSocket.close();
  }, [user, appointmentId, token]);

  const handleSendMessage = async (e) => {
    e.preventDefault();
    if (!newMessage.trim()) return;

    const msgText = newMessage;
    setNewMessage('');
    
    // Optimistic UI update
    setMessages(prev => [...prev, { text: msgText, senderId: user.id }]);

    try {
      await fetch('http://localhost:3000/chat/sendMessage', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({
          appointmentId,
          reciver: receiverId, // NOTE: backend spells it 'reciver'
          sender: user.id,
          message: msgText
        })
      });
    } catch (err) {
      console.error("Failed to send message:", err);
    }
  };

  const handleFileUpload = async (e) => {
    const file = e.target.files[0];
    if (!file) return;

    setUploading(true);
    const formData = new FormData();
    formData.append('file', file);

    try {
      const res = await fetch('http://localhost:3000/upload', {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`
          // Don't set Content-Type for FormData, browser does it automatically
        },
        body: formData
      });
      const data = await res.json();
      if (res.ok && data.file) {
        // Send a message containing the link to the file
        const fileLink = `http://localhost:3000/files/${data.file.filename}`;
        
        // Optimistic UI update
        const attachmentMsg = `[Attachment]: ${fileLink}`;
        setMessages(prev => [...prev, { text: attachmentMsg, senderId: user.id }]);

        // Send via chat service
        await fetch('http://localhost:3000/chat/sendMessage', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json', 'Authorization': `Bearer ${token}` },
          body: JSON.stringify({ appointmentId, reciver: receiverId, sender: user.id, message: attachmentMsg })
        });
      } else {
        alert("Upload failed: " + (data.error || "Unknown error"));
      }
    } catch (err) {
      alert("Upload error: " + err.message);
    } finally {
      setUploading(false);
      e.target.value = null; // Reset input
    }
  };

  return (
    <div style={{ display: 'flex', flexDirection: 'column', height: '400px', border: '1px solid var(--surface-border)', borderRadius: 'var(--radius-md)', backgroundColor: 'var(--surface-color)' }}>
      {/* Chat Messages Area */}
      <div style={{ flex: 1, padding: '1rem', overflowY: 'auto', display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
        {messages.map((msg, idx) => {
          const isMine = msg.senderId === user.id;
          return (
            <div key={idx} style={{ alignSelf: isMine ? 'flex-end' : 'flex-start', maxWidth: '75%' }}>
              <div style={{ 
                backgroundColor: isMine ? 'var(--primary-color)' : 'var(--bg-color)', 
                color: isMine ? 'white' : 'var(--text-primary)',
                padding: '0.75rem 1rem', 
                borderRadius: 'var(--radius-lg)',
                borderBottomRightRadius: isMine ? '0' : 'var(--radius-lg)',
                borderBottomLeftRadius: isMine ? 'var(--radius-lg)' : '0'
              }}>
                {msg.text.startsWith('[Attachment]:') ? (
                  <a href={msg.text.replace('[Attachment]: ', '')} target="_blank" rel="noreferrer" style={{ color: 'inherit', textDecoration: 'underline' }}>
                    View Attachment
                  </a>
                ) : (
                  msg.text
                )}
              </div>
            </div>
          );
        })}
        <div ref={messagesEndRef} />
      </div>

      {/* Input Area */}
      <div style={{ borderTop: '1px solid var(--surface-border)', padding: '0.75rem', display: 'flex', gap: '0.5rem', alignItems: 'center' }}>
        <label className="btn btn-outline" style={{ cursor: 'pointer', padding: '0.5rem', display: 'flex', alignItems: 'center' }}>
          <span title="Upload File">📎</span>
          <input type="file" style={{ display: 'none' }} onChange={handleFileUpload} disabled={uploading} />
        </label>
        
        <form onSubmit={handleSendMessage} style={{ flex: 1, display: 'flex', gap: '0.5rem' }}>
          <input 
            value={newMessage} 
            onChange={e => setNewMessage(e.target.value)} 
            placeholder="Type a message..." 
            style={{ flex: 1 }}
          />
          <button type="submit" className="btn btn-primary" disabled={!newMessage.trim() || uploading}>Send</button>
        </form>
      </div>
    </div>
  );
}
