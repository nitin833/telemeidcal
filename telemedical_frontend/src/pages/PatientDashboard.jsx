import React, { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';
import { useSelector, useDispatch } from 'react-redux';
import { selectAuth, updateUser } from '../store/authSlice';
import Chat from '../components/Chat';

export default function PatientDashboard() {
  const api = useApi();
  const { user, token } = useSelector(selectAuth);
  const dispatch = useDispatch();

  const [activeTab, setActiveTab] = useState('book');
  
  // Book State
  const [hospitals, setHospitals] = useState([]);
  const [selectedHospitalId, setSelectedHospitalId] = useState('');
  const [doctorIdToBook, setDoctorIdToBook] = useState('');
  const [bookingMsg, setBookingMsg] = useState('');

  // Appointments / Prescriptions
  const [appointments, setAppointments] = useState([]);
  const [activeChatAppt, setActiveChatAppt] = useState(null);
  
  // Profile Update
  const [profileUsername, setProfileUsername] = useState(user?.username || '');
  const [profileEmail, setProfileEmail] = useState('');
  const [profilePassword, setProfilePassword] = useState('');

  useEffect(() => {
    if (activeTab === 'book') {
      fetchHospitals();
    } else if (activeTab === 'appointments') {
      fetchMyAppointments();
    }
  }, [activeTab]);

  const fetchHospitals = async () => {
    try {
      const data = await api('/hospital/?param=all');
      setHospitals(Array.isArray(data) ? data : []);
    } catch (e) {
      console.error(e);
    }
  };

  const fetchMyAppointments = async () => {
    try {
      const data = await api(`/appointments/byPatient?patientId=${user.id}`);
      setAppointments(Array.isArray(data) ? data : []);
    } catch (e) {
      console.error(e);
    }
  };

  const handleBookAppointment = async (e) => {
    e.preventDefault();
    setBookingMsg("Booking...");
    try {
      await api('/appointments/', { 
        method: 'POST', 
        body: { patientId: user.id, doctorId: doctorIdToBook }
      });
      setBookingMsg("Appointment booked successfully! Waiting for doctor to accept.");
      setDoctorIdToBook('');
    } catch (err) {
      setBookingMsg("Failed: " + err.message);
    }
  };

  const handleUpdateProfile = async (e) => {
    e.preventDefault();
    try {
      await api(`/user/${user.id}/user`, { method: 'PUT', body: { username: profileUsername, email: profileEmail, password: profilePassword } });
      dispatch(updateUser({ username: profileUsername }));
      alert("Profile updated.");
    } catch (err) {
      alert("Failed to update profile: " + err.message);
    }
  };

  return (
    <div className="page-container animate-fade-in">
      <h1 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Patient Dashboard</h1>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button className={`btn ${activeTab === 'book' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('book')}>Book Appointment</button>
        <button className={`btn ${activeTab === 'appointments' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('appointments')}>My Appointments & Prescriptions</button>
        <button className={`btn ${activeTab === 'profile' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('profile')}>My Profile</button>
      </div>

      {activeTab === 'book' && (
        <div className="grid-2">
          <div className="glass-card">
            <h2>Hospitals</h2>
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
              {hospitals.map(h => (
                <div key={h.id} style={{ border: '1px solid var(--surface-border)', padding: '1rem', borderRadius: 'var(--radius-md)', cursor: 'pointer', backgroundColor: selectedHospitalId === h.id ? 'var(--primary-light)' : 'transparent' }} onClick={() => setSelectedHospitalId(h.id)}>
                  <h4 style={{ margin: 0 }}>{h.name}</h4>
                  <p style={{ margin: 0, fontSize: '0.875rem' }}>{h.description}</p>
                </div>
              ))}
              {hospitals.length === 0 && <p>No hospitals found.</p>}
            </div>
          </div>

          <div className="glass-card">
            <h2>Book a Doctor</h2>
            {selectedHospitalId ? (
              <form onSubmit={handleBookAppointment}>
                {bookingMsg && <div style={{ padding: '0.75rem', backgroundColor: 'var(--primary-light)', color: 'var(--primary-color)', marginBottom: '1rem', borderRadius: 'var(--radius-sm)' }}>{bookingMsg}</div>}
                <div className="input-group">
                  <label>Doctor UUID</label>
                  <input value={doctorIdToBook} onChange={e => setDoctorIdToBook(e.target.value)} placeholder="Enter Doctor ID" required />
                  <small style={{ marginTop: '0.5rem', color: 'var(--text-secondary)' }}>
                    In a full app, selecting a hospital would fetch and display its list of doctors here for easy selection.
                  </small>
                </div>
                <button type="submit" className="btn btn-primary">Request Appointment</button>
              </form>
            ) : (
              <p>Please select a hospital from the list to see booking options.</p>
            )}
          </div>
        </div>
      )}

      {activeTab === 'appointments' && (
        <div className="glass-card">
          <h2>My Appointments</h2>
          <div className="grid-2" style={{ marginTop: '1.5rem' }}>
            {appointments.map(app => (
              <div key={app.id} style={{ border: '1px solid var(--surface-border)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                <p><strong>Doctor ID:</strong> {app.doctorId}</p>
                <p><strong>Status:</strong> <span className="badge badge-primary">{app.status || 'PENDING'}</span></p>
                {app.prescription && (
                  <div style={{ marginTop: '1rem', padding: '1rem', backgroundColor: 'var(--secondary-light)', borderRadius: 'var(--radius-md)' }}>
                    <h4 style={{ color: 'var(--secondary-hover)', margin: 0 }}>Prescription</h4>
                    <p style={{ margin: 0, marginTop: '0.5rem' }}>{app.prescription.text}</p>
                  </div>
                )}
                <div style={{ marginTop: '1rem' }}>
                  <button className="btn btn-outline" onClick={() => setActiveChatAppt(app)}>Open Chat</button>
                </div>
              </div>
            ))}
            {appointments.length === 0 && <p>You have no appointments.</p>}
          </div>

          {activeChatAppt && (
            <div style={{ marginTop: '2rem' }}>
              <h3 style={{ marginBottom: '1rem' }}>Chat with Doctor ({activeChatAppt.doctorId})</h3>
              <Chat appointmentId={activeChatAppt.id} receiverId={activeChatAppt.doctorId} />
            </div>
          )}
        </div>
      )}

      {activeTab === 'profile' && (
        <div className="glass-card" style={{ maxWidth: '500px' }}>
          <h2>Update Profile</h2>
          <form onSubmit={handleUpdateProfile}>
             <div className="input-group"><label>New Username</label><input value={profileUsername} onChange={e=>setProfileUsername(e.target.value)} required /></div>
             <div className="input-group"><label>New Email</label><input type="email" value={profileEmail} onChange={e=>setProfileEmail(e.target.value)} required /></div>
             <div className="input-group"><label>New Password</label><input type="password" value={profilePassword} onChange={e=>setProfilePassword(e.target.value)} required /></div>
             <button type="submit" className="btn btn-primary">Save Changes</button>
          </form>
        </div>
      )}
    </div>
  );
}
