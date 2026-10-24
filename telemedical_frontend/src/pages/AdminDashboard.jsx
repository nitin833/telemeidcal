import React, { useState, useEffect } from 'react';
import { useApi } from '../hooks/useApi';

export default function AdminDashboard() {
  const api = useApi();
  const [activeTab, setActiveTab] = useState('hospitals');
  
  // Hospitals state
  const [hospitals, setHospitals] = useState([]);
  const [loading, setLoading] = useState(false);
  
  // Manage User state
  const [userId, setUserId] = useState('');
  const [userResult, setUserResult] = useState(null);

  // New Hospital State
  const [newHospName, setNewHospName] = useState('');
  const [newHospDesc, setNewHospDesc] = useState('');
  const [ownerUsername, setOwnerUsername] = useState('');
  const [ownerEmail, setOwnerEmail] = useState('');
  const [ownerPassword, setOwnerPassword] = useState('');
  const [creationMessage, setCreationMessage] = useState('');

  useEffect(() => {
    if (activeTab === 'hospitals') {
      fetchHospitals();
    }
  }, [activeTab]);

  const fetchHospitals = async () => {
    setLoading(true);
    try {
      const data = await api('/hospital/?param=all');
      setHospitals(Array.isArray(data) ? data : []);
    } catch (e) {
      console.error(e);
    } finally {
      setLoading(false);
    }
  };

  const handleCreateHospitalAndOwner = async (e) => {
    e.preventDefault();
    setCreationMessage('Creating...');
    try {
      // 1. Create hospital
      const hosp = await api('/hospital/', { method: 'POST', body: { name: newHospName, description: newHospDesc } });
      
      // 2. Create Owner (User)
      // Note: we might need a specific /user/addOwner endpoint, but using addUser if that's what's available
      const owner = await api('/user/addUser', { method: 'POST', body: { username: ownerUsername, email: ownerEmail, password: ownerPassword } });
      
      // 3. Link them
      if (hosp?.id && owner?.id) {
        await api(`/hospital/addOwner/${hosp.id}/${owner.id}`, { method: 'PUT' });
      }
      
      setCreationMessage('Hospital and Owner created successfully!');
      setNewHospName(''); setNewHospDesc(''); setOwnerUsername(''); setOwnerEmail(''); setOwnerPassword('');
      fetchHospitals();
    } catch (err) {
      setCreationMessage(`Error: ${err.message}`);
    }
  };

  const handleDeleteHospital = async (id) => {
    if (!window.confirm("Delete this hospital?")) return;
    try {
      await api(`/hospital/${id}`, { method: 'DELETE' });
      fetchHospitals();
    } catch (e) {
      alert("Failed to delete: " + e.message);
    }
  };

  const handleLookupUser = async (e) => {
    e.preventDefault();
    try {
      const u = await api(`/user/${userId}`);
      setUserResult(u);
    } catch (e) {
      alert("User not found or error: " + e.message);
    }
  };

  const handleDeleteUser = async () => {
    if (!window.confirm("Delete this user permanently?")) return;
    try {
      await api(`/user/${userId}`, { method: 'DELETE' });
      setUserResult(null);
      alert('User deleted.');
    } catch (e) {
      alert('Delete failed: ' + e.message);
    }
  };

  return (
    <div className="page-container animate-fade-in">
      <h1 style={{ marginBottom: '1.5rem', color: 'var(--primary-color)' }}>Admin Dashboard</h1>
      
      <div style={{ display: 'flex', gap: '1rem', marginBottom: '2rem' }}>
        <button className={`btn ${activeTab === 'hospitals' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('hospitals')}>Manage Hospitals</button>
        <button className={`btn ${activeTab === 'create' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('create')}>Create Hospital & Owner</button>
        <button className={`btn ${activeTab === 'users' ? 'btn-primary' : 'btn-outline'}`} onClick={() => setActiveTab('users')}>Manage Users</button>
      </div>

      {activeTab === 'hospitals' && (
        <div className="glass-card">
          <h2>Hospitals Directory</h2>
          {loading ? <p>Loading...</p> : (
            <div className="grid-3">
              {hospitals.map(h => (
                <div key={h.id} style={{ border: '1px solid var(--surface-border)', padding: '1rem', borderRadius: 'var(--radius-md)' }}>
                  <h4>{h.name}</h4>
                  <p style={{ fontSize: '0.875rem' }}>{h.description}</p>
                  <p style={{ fontSize: '0.75rem', color: 'var(--text-secondary)' }}>ID: {h.id}</p>
                  <button onClick={() => handleDeleteHospital(h.id)} className="btn btn-danger" style={{ padding: '0.25rem 0.75rem', fontSize: '0.75rem', marginTop: '0.5rem' }}>Delete Hospital</button>
                </div>
              ))}
              {hospitals.length === 0 && <p>No hospitals found.</p>}
            </div>
          )}
        </div>
      )}

      {activeTab === 'create' && (
        <div className="glass-card" style={{ maxWidth: '600px' }}>
          <h2>Onboard New Hospital & Owner</h2>
          {creationMessage && <div style={{ padding: '1rem', backgroundColor: 'var(--secondary-light)', color: 'var(--secondary-hover)', marginBottom: '1rem', borderRadius: 'var(--radius-sm)' }}>{creationMessage}</div>}
          <form onSubmit={handleCreateHospitalAndOwner}>
            <div className="grid-2">
              <div className="input-group">
                <label>Hospital Name</label>
                <input value={newHospName} onChange={e => setNewHospName(e.target.value)} required />
              </div>
              <div className="input-group">
                <label>Hospital Description</label>
                <input value={newHospDesc} onChange={e => setNewHospDesc(e.target.value)} required />
              </div>
            </div>
            
            <h3 style={{ marginTop: '1.5rem', marginBottom: '1rem' }}>Owner Credentials</h3>
            <div className="grid-2">
              <div className="input-group">
                <label>Username</label>
                <input value={ownerUsername} onChange={e => setOwnerUsername(e.target.value)} required />
              </div>
              <div className="input-group">
                <label>Email</label>
                <input type="email" value={ownerEmail} onChange={e => setOwnerEmail(e.target.value)} required />
              </div>
              <div className="input-group" style={{ gridColumn: 'span 2' }}>
                <label>Password</label>
                <input type="password" value={ownerPassword} onChange={e => setOwnerPassword(e.target.value)} required />
              </div>
            </div>
            <button type="submit" className="btn btn-primary" style={{ marginTop: '1rem' }}>Create Everything</button>
          </form>
        </div>
      )}

      {activeTab === 'users' && (
        <div className="glass-card" style={{ maxWidth: '600px' }}>
          <h2>Manage Users</h2>
          <form onSubmit={handleLookupUser} style={{ display: 'flex', gap: '0.5rem', marginBottom: '1.5rem' }}>
            <input placeholder="Enter User UUID..." value={userId} onChange={e => setUserId(e.target.value)} required style={{ flex: 1 }} />
            <button type="submit" className="btn btn-primary">Lookup User</button>
          </form>
          
          {userResult && (
            <div style={{ padding: '1.5rem', backgroundColor: 'var(--primary-light)', borderRadius: 'var(--radius-md)' }}>
              <h4>{userResult.username}</h4>
              <p style={{ margin: '0.5rem 0' }}>Email: {userResult.email}</p>
              <div style={{ display: 'flex', gap: '1rem', marginTop: '1rem' }}>
                <button onClick={handleDeleteUser} className="btn btn-danger">Delete User</button>
              </div>
            </div>
          )}
        </div>
      )}
    </div>
  );
}
