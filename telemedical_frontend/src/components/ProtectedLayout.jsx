import React from 'react';
import { Outlet, Link, useNavigate } from 'react-router-dom';
import { useSelector, useDispatch } from 'react-redux';
import { selectAuth, logout } from '../store/authSlice';

export default function ProtectedLayout() {
  const { user } = useSelector(selectAuth);
  const dispatch = useDispatch();
  const navigate = useNavigate();

  const handleLogout = () => {
    dispatch(logout());
    navigate('/');
  };

  const getDashboardPath = () => {
    if (!user) return '/';
    if (user.role === 'ROLE_ADMIN') return '/admin';
    if (user.role === 'ROLE_OWNER') return '/owner';
    if (user.role === 'ROLE_DOCTOR') return '/doctor';
    return '/patient';
  };

  return (
    <div className="app-container">
      {/* Sidebar */}
      <div style={{
        width: '260px',
        backgroundColor: 'var(--surface-color)',
        borderRight: '1px solid var(--surface-border)',
        padding: '1.5rem',
        display: 'flex',
        flexDirection: 'column',
        boxShadow: 'var(--shadow-sm)'
      }}>
        <div style={{ display: 'flex', alignItems: 'center', gap: '0.5rem', marginBottom: '2.5rem' }}>
          <div style={{ width: '32px', height: '32px', backgroundColor: 'var(--primary-color)', borderRadius: '8px', display: 'flex', alignItems: 'center', justifyContent: 'center', color: 'white', fontWeight: 'bold' }}>
            T
          </div>
          <h2 style={{ color: 'var(--primary-color)', margin: 0, fontSize: '1.25rem' }}>TeleMed</h2>
        </div>
        
        <div style={{ flex: 1, display: 'flex', flexDirection: 'column', gap: '0.5rem' }}>
          <Link to={getDashboardPath()} style={{ 
            textDecoration: 'none', 
            color: 'var(--text-primary)', 
            fontWeight: 500, 
            padding: '0.75rem 1rem', 
            borderRadius: 'var(--radius-md)',
            backgroundColor: 'var(--primary-light)',
            display: 'flex',
            alignItems: 'center',
            gap: '0.5rem'
          }}>
            Dashboard
          </Link>
          {/* We will add more specific links later based on role */}
        </div>

        <div style={{ borderTop: '1px solid var(--surface-border)', paddingTop: '1.5rem', marginTop: 'auto' }}>
          <div style={{ marginBottom: '1rem' }}>
            <p style={{ margin: 0, fontSize: '0.875rem', color: 'var(--text-secondary)' }}>Logged in as</p>
            <div style={{ fontWeight: 600, color: 'var(--text-primary)' }}>{user?.username}</div>
            <div className="badge badge-primary" style={{ marginTop: '0.5rem' }}>
              {user?.role?.replace('ROLE_', '')}
            </div>
          </div>
          <button onClick={handleLogout} className="btn btn-outline" style={{ width: '100%', justifyContent: 'center' }}>
            Logout
          </button>
        </div>
      </div>

      {/* Main Content Area */}
      <div className="main-content">
        <Outlet />
      </div>
    </div>
  );
}
