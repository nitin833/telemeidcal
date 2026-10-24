import React from 'react';
import { createBrowserRouter, redirect } from 'react-router-dom';
import Login from '../pages/Login';
import AdminDashboard from '../pages/AdminDashboard';
import OwnerDashboard from '../pages/OwnerDashboard';
import DoctorDashboard from '../pages/DoctorDashboard';
import PatientDashboard from '../pages/PatientDashboard';
import PublicDoctor from '../pages/PublicDoctor';
import PublicHospital from '../pages/PublicHospital';
import ProtectedLayout from '../components/ProtectedLayout';
import { store } from '../store';

const protectedLoader = (allowedRoles) => () => {
  const state = store.getState();
  const { isAuthenticated, user } = state.auth;
  
  if (!isAuthenticated) {
    return redirect('/');
  }
  
  if (allowedRoles && !allowedRoles.includes(user?.role)) {
    return redirect('/');
  }
  
  return null; // Let the route render
};

const router = createBrowserRouter([
  {
    path: '/',
    element: <Login />,
    loader: () => {
       const state = store.getState();
       if (state.auth.isAuthenticated) {
          const role = state.auth.user?.role;
          if (role === 'ROLE_ADMIN') return redirect('/admin');
          if (role === 'ROLE_OWNER') return redirect('/owner');
          if (role === 'ROLE_DOCTOR') return redirect('/doctor');
          return redirect('/patient');
       }
       return null;
    }
  },
  {
    path: '/doctor-profile/:id',
    element: <PublicDoctor />
  },
  {
    path: '/hospital-profile/:id',
    element: <PublicHospital />
  },
  {
    path: '/admin',
    element: <ProtectedLayout />,
    loader: protectedLoader(['ROLE_ADMIN']),
    children: [
      { index: true, element: <AdminDashboard /> },
    ]
  },
  {
    path: '/owner',
    element: <ProtectedLayout />,
    loader: protectedLoader(['ROLE_OWNER']),
    children: [
      { index: true, element: <OwnerDashboard /> },
    ]
  },
  {
    path: '/doctor',
    element: <ProtectedLayout />,
    loader: protectedLoader(['ROLE_DOCTOR']),
    children: [
      { index: true, element: <DoctorDashboard /> },
    ]
  },
  {
    path: '/patient',
    element: <ProtectedLayout />,
    loader: protectedLoader(['ROLE_USER', 'ROLE_PATIENT']), // Typically 'ROLE_USER' is the default
    children: [
      { index: true, element: <PatientDashboard /> },
    ]
  }
]);

export default router;
