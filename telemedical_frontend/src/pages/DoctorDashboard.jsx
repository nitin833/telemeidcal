import React, { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';
import { useSelector } from 'react-redux';
import { selectAuth } from '../store/authSlice';
import Chat from '../components/Chat';

export default function DoctorDashboard() {
  const api = useApi();
  const { user } = useSelector(selectAuth);
  
  const [activeTab, setActiveTab] = useState('appointments');
  const [appointments, setAppointments] = useState([]);
  
  const [prescriptionApptId, setPrescriptionApptId] = useState('');
  const [prescriptionText, setPrescriptionText] = useState('');

  const [activeChatAppt, setActiveChatAppt] = useState(null);
  
  useEffect(() => {
    if (activeTab === 'appointments' && user?.id) {
      fetchAppointments();
    }
  }, [activeTab, user]);

  const fetchAppointments = async () => {
    try {
      const data = await api(`/appointments/byDoctor?doctorId=${user.id}`, { method: 'GET' });
      setAppointments(Array.isArray(data) ? data : []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleAcceptAppointment = async (id) => {
    try {
      await api(`/appointments/${id}/accept`, { method: 'PUT' });
      alert("Appointment Accepted");
      fetchAppointments();
    } catch (e) {
      alert("Failed: " + e.message);
    }
  };

  const handleCancelAppointment = async (id) => {
    try {
      await api(`/appointments/${id}/cancel`, { method: 'PUT' });
      alert("Appointment Cancelled");
      fetchAppointments();
    } catch (e) {
      alert("Failed: " + e.message);
    }
  };

  const handlePrescribe = async (e) => {
    e.preventDefault();
    try {
      await api(`/appointments/${prescriptionApptId}/prescription`, { 
        method: 'POST', 
        body: { text: prescriptionText } 
      });
      alert("Prescription saved.");
      setPrescriptionText('');
      setPrescriptionApptId('');
    } catch (err) {
      alert("Error: " + err.message);
    }
  };

  return (
    <div className="page-container animate-fade-in">
      <h1 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Doctor Dashboard</h1>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button className={`btn ${activeTab === 'appointments' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('appointments')}>My Appointments</button>
        <button className={`btn ${activeTab === 'prescribe' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('prescribe')}>Write Prescription</button>
      </div>

      {activeTab === 'appointments' && (
        <div className="glass-card">
          <h2>Upcoming Appointments</h2>
          <div className="grid-2" style={{ marginTop: '1.5rem' }}>
            {appointments.map(app => (
              <div key={app.id} style={{ border: '1px solid var(--surface-border)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                <p><strong>Patient ID:</strong> {app.patientId}</p>
                <p><strong>Status:</strong> <span className="badge badge-secondary">{app.status || 'PENDING'}</span></p>
                <div style={{ display: 'flex', gap: '0.5rem', marginTop: '1rem' }}>
                  <button className="btn btn-primary" style={{ padding: '0.5rem' }} onClick={() => handleAcceptAppointment(app.id)}>Accept</button>
                  <button className="btn btn-danger" style={{ padding: '0.5rem' }} onClick={() => handleCancelAppointment(app.id)}>Cancel</button>
                  <button className="btn btn-outline" style={{ padding: '0.5rem' }} onClick={() => setActiveChatAppt(app)}>Chat</button>
                </div>
              </div>
            ))}
            {appointments.length === 0 && <p>No appointments found.</p>}
          </div>

          {activeChatAppt && (
            <div style={{ marginTop: '2rem' }}>
              <h3 style={{ marginBottom: '1rem' }}>Chat with Patient ({activeChatAppt.patientId})</h3>
              <Chat appointmentId={activeChatAppt.id} receiverId={activeChatAppt.patientId} />
            </div>
          )}
        </div>
      )}

      {activeTab === 'prescribe' && (
        <div className="glass-card" style={{ maxWidth: '600px' }}>
          <h2>Write Prescription</h2>
          <form onSubmit={handlePrescribe}>
            <div className="input-group">
              <label>Appointment ID</label>
              <input value={prescriptionApptId} onChange={e => setPrescriptionApptId(e.target.value)} required />
            </div>
            <div className="input-group">
              <label>Prescription Details</label>
              <textarea rows={6} value={prescriptionText} onChange={e => setPrescriptionText(e.target.value)} required />
            </div>
            <button type="submit" className="btn btn-primary">Save Prescription</button>
          </form>
        </div>
      )}
    </div>
  );
}
