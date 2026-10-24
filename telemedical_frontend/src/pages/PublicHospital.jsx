import React, { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { apiFetch } from '../api';

export default function PublicHospital() {
  const { id } = useParams();
  const [hospital, setHospital] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchHospital = async () => {
      try {
        const data = await apiFetch(`/hospital/${id}`, { method: 'GET' });
        setHospital(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchHospital();
  }, [id]);

  if (loading) return <div className="page-container">Loading...</div>;
  if (!hospital) return <div className="page-container">Hospital not found.</div>;

  return (
    <div className="page-container animate-fade-in">
      <div className="glass-card" style={{ maxWidth: '900px', margin: '0 auto' }}>
        <h1 style={{ color: 'var(--primary-color)', marginBottom: '0.5rem' }}>{hospital.name}</h1>
        <p style={{ color: 'var(--text-secondary)', fontSize: '1.125rem' }}>{hospital.description}</p>
        
        <div style={{ marginTop: '3rem' }}>
          <h2 style={{ borderBottom: '1px solid var(--surface-border)', paddingBottom: '0.5rem', marginBottom: '1.5rem' }}>Our Doctors</h2>
          
          {hospital.doctors && hospital.doctors.length > 0 ? (
            <div className="grid-2">
              {hospital.doctors.map(doc => (
                <div key={doc.id} style={{ border: '1px solid var(--surface-border)', padding: '1.5rem', borderRadius: 'var(--radius-md)' }}>
                  <h3 style={{ margin: 0 }}>Dr. {doc.firstname} {doc.lastname}</h3>
                  <Link to={`/doctor-profile/${doc.id}`} className="btn btn-outline" style={{ marginTop: '1rem', width: '100%' }}>View Profile</Link>
                </div>
              ))}
            </div>
          ) : (
            <p>No doctors are currently listed for this hospital.</p>
          )}
        </div>
      </div>
    </div>
  );
}
