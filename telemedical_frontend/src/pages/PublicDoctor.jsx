import React, { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { apiFetch } from '../api';

export default function PublicDoctor() {
  const { id } = useParams();
  const [doctor, setDoctor] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const fetchDoctor = async () => {
      try {
        const data = await apiFetch(`/user/${id}`, { method: 'GET' });
        setDoctor(data);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    fetchDoctor();
  }, [id]);

  if (loading) return <div className="page-container">Loading...</div>;
  if (!doctor) return <div className="page-container">Doctor not found.</div>;

  return (
    <div className="page-container animate-fade-in">
      <div className="glass-card" style={{ maxWidth: '800px', margin: '0 auto', textAlign: 'center' }}>
        <div style={{ width: '100px', height: '100px', backgroundColor: 'var(--primary-color)', borderRadius: '50%', margin: '0 auto 1.5rem', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontSize: '2.5rem', fontWeight: 'bold' }}>
          {doctor.firstname ? doctor.firstname[0] : 'D'}
        </div>
        <h1 style={{ color: 'var(--text-primary)', marginBottom: '0.5rem' }}>Dr. {doctor.firstname} {doctor.lastname}</h1>
        <p style={{ color: 'var(--text-secondary)', marginBottom: '2rem' }}>Contact: {doctor.email}</p>
        
        <div style={{ borderTop: '1px solid var(--surface-border)', paddingTop: '2rem', textAlign: 'left' }}>
          <h2 style={{ color: 'var(--primary-color)' }}>Qualifications & Degrees</h2>
          {doctor.qualifications && doctor.qualifications.length > 0 ? (
            <ul style={{ listStyleType: 'none', padding: 0, marginTop: '1rem', display: 'flex', flexWrap: 'wrap', gap: '0.5rem' }}>
              {doctor.qualifications.map((q, idx) => (
                <li key={idx} className="badge badge-secondary" style={{ padding: '0.5rem 1rem', fontSize: '0.875rem' }}>
                  {q.name}
                </li>
              ))}
            </ul>
          ) : (
            <p>No qualifications listed yet.</p>
          )}
        </div>
      </div>
    </div>
  );
}
