import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import ItemCard from '../components/ItemCard';
import Toast from '../components/Toast';

const Profile = () => {
  const [userReports, setUserReports] = useState([]);
  const [loading, setLoading] = useState(true);
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState('');
  const [toastType, setToastType] = useState('success');

  useEffect(() => {
    fetchUserReports();
  }, []);

  const fetchUserReports = async () => {
    try {
      const response = await axios.get('http://localhost:8080/api/user/reports');
      setUserReports(response.data);
    } catch (error) {
      console.error('Error fetching user reports:', error);
      // Fallback to static data if API fails
      setUserReports([
        {
          id: 1,
          itemType: 'iPhone 13 Pro',
          description: 'Black iPhone 13 Pro with red case, lost in Central Park',
          location: 'Central Park, New York',
          date: '2025-08-01',
          status: 'Lost',
          contact: 'john@email.com',
          image: null
        },
        {
          id: 2,
          itemType: 'Car Keys',
          description: 'Toyota car keys with black keychain',
          location: 'Shopping Center Parking',
          date: '2025-07-31',
          status: 'Lost',
          contact: 'john@email.com',
          image: null
        }
      ]);
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteReport = async (reportId) => {
    try {
      await axios.delete(`http://localhost:8080/api/user/reports/${reportId}`);
      setUserReports(userReports.filter(report => report.id !== reportId));
      setToastMessage('Report deleted successfully!');
      setToastType('success');
      setShowToast(true);
    } catch (error) {
      console.error('Error deleting report:', error);
      setToastMessage('Failed to delete report. Please try again.');
      setToastType('error');
      setShowToast(true);
    }
  };

  return (
    <div className="page-container">
      <div className="main-content">
        {/* Header */}
        <div className="mb-8">
          <h1 className="text-heading">My Profile</h1>
          <p className="text-subheading">Manage your submitted reports and account information</p>
        </div>

        {/* Profile Info */}
        <div className="card mb-8">
          <div className="flex items-center space-x-4">
            <div className="w-16 h-16 bg-blue-100 rounded-full flex items-center justify-center shadow-sm">
              <svg className="w-8 h-8 text-blue-600" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 9a3 3 0 100-6 3 3 0 000 6zm-7 9a7 7 0 1114 0H3z" clipRule="evenodd" />
              </svg>
            </div>
            <div>
              <h2 className="text-xl font-semibold text-gray-800">John Doe</h2>
              <p className="text-gray-600">john@email.com</p>
              <p className="text-sm text-gray-500">Member since August 2025</p>
            </div>
          </div>
        </div>

        {/* Stats */}
        <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
          <div className="card text-center">
            <div className="text-2xl font-bold text-blue-600 mb-1">{userReports.length}</div>
            <div className="text-gray-600">Total Reports</div>
          </div>
          <div className="card text-center">
            <div className="text-2xl font-bold text-green-600 mb-1">
              {userReports.filter(r => r.status === 'Found').length}
            </div>
            <div className="text-gray-600">Items Found</div>
          </div>
          <div className="card text-center">
            <div className="text-2xl font-bold text-red-600 mb-1">
              {userReports.filter(r => r.status === 'Lost').length}
            </div>
            <div className="text-gray-600">Items Lost</div>
          </div>
        </div>

        {/* User Reports */}
        <div className="mb-8">
          <div className="flex justify-between items-center mb-6">
            <h2 className="text-2xl font-bold text-gray-800">My Reports</h2>
            <Link to="/report" className="btn-primary">
              Report New Item
            </Link>
          </div>

          {loading ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
              {[1, 2, 3].map((i) => (
                <div key={i} className="card animate-pulse">
                  <div className="h-48 bg-gray-200 rounded-lg mb-4"></div>
                  <div className="space-y-3">
                    <div className="h-4 bg-gray-200 rounded w-3/4"></div>
                    <div className="h-3 bg-gray-200 rounded w-1/2"></div>
                    <div className="h-3 bg-gray-200 rounded w-2/3"></div>
                  </div>
                </div>
              ))}
            </div>
          ) : userReports.length > 0 ? (
            <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
              {userReports.map((report) => (
                <div key={report.id} className="card relative">
                  <ItemCard item={report} />
                  
                  {/* Action Buttons */}
                  <div className="absolute top-4 right-4 flex space-x-2">
                    <button
                      onClick={() => handleDeleteReport(report.id)}
                      className="btn-danger text-xs py-1 px-2"
                      title="Delete Report"
                    >
                      <svg className="w-4 h-4" fill="currentColor" viewBox="0 0 20 20">
                        <path fillRule="evenodd" d="M9 2a1 1 0 00-.894.553L7.382 4H4a1 1 0 000 2v10a2 2 0 002 2h8a2 2 0 002-2V6a1 1 0 100-2h-3.382l-.724-1.447A1 1 0 0011 2H9zM7 8a1 1 0 012 0v6a1 1 0 11-2 0V8zm5-1a1 1 0 00-1 1v6a1 1 0 102 0V8a1 1 0 00-1-1z" clipRule="evenodd" />
                      </svg>
                    </button>
                  </div>
                </div>
              ))}
            </div>
          ) : (
            <div className="text-center py-12">
              <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4 shadow-sm">
                <svg className="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clipRule="evenodd" />
                </svg>
              </div>
              <h3 className="text-lg font-medium text-gray-800 mb-2">No Reports Yet</h3>
              <p className="text-gray-600 mb-6">
                You haven't submitted any reports yet. Start by reporting a lost or found item.
              </p>
              <Link to="/report" className="btn-primary">
                Report Your First Item
              </Link>
            </div>
          )}
        </div>

        {/* Account Actions */}
        <div className="card">
          <h3 className="text-lg font-semibold text-gray-800 mb-4">Account Actions</h3>
          <div className="space-y-3">
            <button className="btn-secondary w-full text-left">
              <div className="flex items-center">
                <svg className="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M11.49 3.17c-.38-1.56-2.6-1.56-2.98 0a1.532 1.532 0 01-2.286.948c-1.372-.836-2.942.734-2.106 2.106.54.886.061 2.042-.947 2.287-1.561.379-1.561 2.6 0 2.978a1.532 1.532 0 01.947 2.287c-.836 1.372.734 2.942 2.106 2.106a1.532 1.532 0 012.287.947c.379 1.561 2.6 1.561 2.978 0a1.533 1.533 0 012.287-.947c1.372.836 2.942-.734 2.106-2.106a1.533 1.533 0 01.947-2.287c1.561-.379 1.561-2.6 0-2.978a1.532 1.532 0 01-.947-2.287c.836-1.372-.734-2.942-2.106-2.106a1.532 1.532 0 01-2.287-.947zM10 13a3 3 0 100-6 3 3 0 000 6z" clipRule="evenodd" />
                </svg>
                Edit Profile
              </div>
            </button>
            <button className="btn-secondary w-full text-left">
              <div className="flex items-center">
                <svg className="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M5 9V7a5 5 0 0110 0v2a2 2 0 012 2v5a2 2 0 01-2 2H5a2 2 0 01-2-2v-5a2 2 0 012-2zm8-2v2H7V7a3 3 0 016 0z" clipRule="evenodd" />
                </svg>
                Change Password
              </div>
            </button>
            <button className="btn-secondary w-full text-left">
              <div className="flex items-center">
                <svg className="w-5 h-5 mr-3" fill="currentColor" viewBox="0 0 20 20">
                  <path fillRule="evenodd" d="M18 10a8 8 0 11-16 0 8 8 0 0116 0zm-2 0c0 .993-.241 1.929-.668 2.754l-1.524-1.525a3.997 3.997 0 00.078-2.183l1.562-1.562A5.996 5.996 0 0116 10z" clipRule="evenodd" />
                </svg>
                Privacy Settings
              </div>
            </button>
          </div>
        </div>
      </div>

      {/* Toast */}
      {showToast && (
        <Toast
          message={toastMessage}
          type={toastType}
          onClose={() => setShowToast(false)}
        />
      )}
    </div>
  );
};

export default Profile; 