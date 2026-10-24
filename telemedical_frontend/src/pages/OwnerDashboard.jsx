import React, { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';
import { useSelector, useDispatch } from 'react-redux';
import { selectAuth, updateUser } from '../store/authSlice';

export default function OwnerDashboard() {
  const api = useApi();
  const { user } = useSelector(selectAuth);
  const dispatch = useDispatch();
  
  const [activeTab, setActiveTab] = useState('doctors');
  
  // Need hospital ID. We'll allow owner to input it if it's not in the token.
  // In a real app, backend would associate Owner to Hospital automatically.
  const [hospitalId, setHospitalId] = useState(localStorage.getItem('owner_hospital_id') || '');

  // Add Doctor State
  const [docUsername, setDocUsername] = useState('');
  const [docEmail, setDocEmail] = useState('');
  const [docPassword, setDocPassword] = useState('');
  const [docFirst, setDocFirst] = useState('');
  const [docLast, setDocLast] = useState('');
  const [docQuals, setDocQuals] = useState('');
  const [doctorMsg, setDoctorMsg] = useState('');

  // Remove Doctor State
  const [removeDocId, setRemoveDocId] = useState('');

  // Profile Update
  const [profileUsername, setProfileUsername] = useState(user?.username || '');
  const [profileEmail, setProfileEmail] = useState('');
  const [profilePassword, setProfilePassword] = useState('');

  const saveHospitalId = (id) => {
    setHospitalId(id);
    localStorage.setItem('owner_hospital_id', id);
  };

  const handleAddDoctor = async (e) => {
    e.preventDefault();
    if (!hospitalId) return setDoctorMsg("Please specify Hospital ID first.");
    setDoctorMsg("Adding doctor...");
    try {
      const qualifications = docQuals.split(',').map(s => ({ name: s.trim() })).filter(Boolean);
      const doctor = await api('/user/addDoctor', { 
        method: 'POST', 
        body: { username: docUsername, email: docEmail, password: docPassword, firstname: docFirst, lastname: docLast, qualifications } 
      });
      
      if (doctor?.id) {
        await api(`/hospital/addDoctor/${hospitalId}/${doctor.id}`, { method: 'PUT' });
        setDoctorMsg(`Doctor ${doctor.username} added to hospital successfully!`);
        setDocUsername(''); setDocEmail(''); setDocPassword(''); setDocFirst(''); setDocLast(''); setDocQuals('');
      }
    } catch (err) {
      setDoctorMsg("Failed: " + err.message);
    }
  };

  const handleRemoveDoctor = async (e) => {
    e.preventDefault();
    if (!hospitalId) return alert("Need Hospital ID");
    try {
      await api(`/hospital/removeDoctor/${hospitalId}/${removeDocId}`, { method: 'PUT' });
      alert("Doctor removed from hospital");
      setRemoveDocId('');
    } catch (err) {
      alert("Remove failed: " + err.message);
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
      <h1 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Owner Dashboard</h1>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button className={`btn ${activeTab === 'doctors' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('doctors')}>Manage Doctors</button>
        <button className={`btn ${activeTab === 'appointments' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('appointments')}>Hospital Appointments</button>
        <button className={`btn ${activeTab === 'profile' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('profile')}>My Profile</button>
      </div>

      <div className="glass-card" style={{ marginBottom: '2rem' }}>
        <label style={{ fontSize: '0.875rem', fontWeight: 600 }}>Active Hospital ID context:</label>
        <input style={{ maxWidth: '300px', marginLeft: '1rem' }} value={hospitalId} onChange={e => saveHospitalId(e.target.value)} placeholder="UUID of your hospital" />
      </div>

      {activeTab === 'doctors' && (
        <div className="grid-2">
          <div className="glass-card">
            <h2>Add New Doctor</h2>
            {doctorMsg && <div style={{ padding: '0.75rem', backgroundColor: 'var(--secondary-light)', color: 'var(--secondary-hover)', marginBottom: '1rem', borderRadius: 'var(--radius-sm)' }}>{doctorMsg}</div>}
            <form onSubmit={handleAddDoctor}>
              <div className="input-group"><label>Username</label><input value={docUsername} onChange={e=>setDocUsername(e.target.value)} required /></div>
              <div className="input-group"><label>Email</label><input type="email" value={docEmail} onChange={e=>setDocEmail(e.target.value)} required /></div>
              <div className="input-group"><label>Password</label><input type="password" value={docPassword} onChange={e=>setDocPassword(e.target.value)} required /></div>
              <div className="input-group"><label>First Name</label><input value={docFirst} onChange={e=>setDocFirst(e.target.value)} required /></div>
              <div className="input-group"><label>Last Name</label><input value={docLast} onChange={e=>setDocLast(e.target.value)} required /></div>
              <div className="input-group"><label>Qualifications (comma separated)</label><input value={docQuals} onChange={e=>setDocQuals(e.target.value)} /></div>
              <button type="submit" className="btn btn-primary" style={{ marginTop: '1rem' }}>Add Doctor</button>
            </form>
          </div>

          <div className="glass-card">
            <h2>Remove Doctor from Hospital</h2>
            <form onSubmit={handleRemoveDoctor}>
              <div className="input-group"><label>Doctor UUID</label><input value={removeDocId} onChange={e=>setRemoveDocId(e.target.value)} required /></div>
              <button type="submit" className="btn btn-danger">Remove Doctor</button>
            </form>
          </div>
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

      {activeTab === 'appointments' && (
        <div className="glass-card">
          <h2>Hospital Appointments</h2>
          <p>This section would list appointments tied to this hospital (backend permitting).</p>
        </div>
      )}
    </div>
  );
}
