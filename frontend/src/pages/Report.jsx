import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import Toast from '../components/Toast';

const Report = () => {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState('');
  const [toastType, setToastType] = useState('success');
  const [formData, setFormData] = useState({
    itemType: '',
    description: '',
    location: '',
    date: new Date().toISOString().split('T')[0],
    status: 'Lost',
    image: null
  });
  const [imagePreview, setImagePreview] = useState(null);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleImageChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setFormData(prev => ({
        ...prev,
        image: file
      }));
      
      // Create preview
      const reader = new FileReader();
      reader.onload = (e) => {
        setImagePreview(e.target.result);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);

    try {
      // Validate form data
      if (!formData.itemType.trim() || !formData.description.trim() || !formData.location.trim()) {
        throw new Error('Please fill in all required fields');
      }

      // Create FormData for file upload
      const submitData = new FormData();
      submitData.append('itemType', formData.itemType.trim());
      submitData.append('description', formData.description.trim());
      submitData.append('location', formData.location.trim());
      submitData.append('date', formData.date);
      submitData.append('status', formData.status);
      if (formData.image) {
        submitData.append('image', formData.image);
      }

      console.log('Submitting report:', {
        itemType: formData.itemType,
        description: formData.description,
        location: formData.location,
        date: formData.date,
        status: formData.status
      });

      const response = await axios.post('http://localhost:8080/api/report', submitData, {
        headers: {
          'Content-Type': 'multipart/form-data'
        },
        timeout: 10000 // 10 second timeout
      });

      console.log('Report submitted successfully:', response.data);

      // Store matched items in localStorage for search results page
      localStorage.setItem('matchedItems', JSON.stringify(response.data.matchedItems || []));
      
      setToastMessage('Report submitted successfully! Redirecting to results...');
      setToastType('success');
      setShowToast(true);
      
      setTimeout(() => {
        navigate('/search');
      }, 1500);

    } catch (error) {
      console.error('Error submitting report:', error);
      
      let errorMessage = 'Failed to submit report. Please try again.';
      
      if (error.response) {
        // Server responded with error
        errorMessage = `Server error: ${error.response.status} - ${error.response.data?.message || 'Unknown error'}`;
      } else if (error.request) {
        // Network error
        errorMessage = 'Network error. Please check your connection and try again.';
      } else if (error.message) {
        // Validation error or other error
        errorMessage = error.message;
      }
      
      setToastMessage(errorMessage);
      setToastType('error');
      setShowToast(true);
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="page-container">
      <div className="main-content">
        <div className="max-w-2xl mx-auto">
          <div className="card">
            <div className="text-center mb-8">
              <div className="logo">
                <div className="w-20 h-20 bg-blue-600 rounded-full flex items-center justify-center mx-auto shadow-lg">
                  <svg className="w-10 h-10 text-white" fill="currentColor" viewBox="0 0 20 20">
                    <path fillRule="evenodd" d="M3 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1zm0 4a1 1 0 011-1h12a1 1 0 110 2H4a1 1 0 01-1-1z" clipRule="evenodd" />
                  </svg>
                </div>
              </div>
              <h1 className="text-heading">Report Item</h1>
              <p className="text-subheading">Report a lost or found item to help connect with your community</p>
            </div>

            <form onSubmit={handleSubmit} className="space-y-6">
              {/* Item Type */}
              <div>
                <label htmlFor="itemType" className="block text-sm font-medium text-gray-700 mb-2">
                  Item Type *
                </label>
                <input
                  type="text"
                  id="itemType"
                  name="itemType"
                  value={formData.itemType}
                  onChange={handleChange}
                  className="input-field"
                  placeholder="e.g., iPhone, Keys, Wallet, Dog"
                  required
                  disabled={loading}
                />
              </div>

              {/* Description */}
              <div>
                <label htmlFor="description" className="block text-sm font-medium text-gray-700 mb-2">
                  Description *
                </label>
                <textarea
                  id="description"
                  name="description"
                  value={formData.description}
                  onChange={handleChange}
                  className="input-field"
                  rows="4"
                  placeholder="Provide detailed description of the item..."
                  required
                  disabled={loading}
                />
              </div>

              {/* Location and Date */}
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div>
                  <label htmlFor="location" className="block text-sm font-medium text-gray-700 mb-2">
                    Location *
                  </label>
                  <input
                    type="text"
                    id="location"
                    name="location"
                    value={formData.location}
                    onChange={handleChange}
                    className="input-field"
                    placeholder="Where was it lost/found?"
                    required
                    disabled={loading}
                  />
                </div>

                <div>
                  <label htmlFor="date" className="block text-sm font-medium text-gray-700 mb-2">
                    Date *
                  </label>
                  <input
                    type="date"
                    id="date"
                    name="date"
                    value={formData.date}
                    onChange={handleChange}
                    className="input-field"
                    required
                    disabled={loading}
                  />
                </div>
              </div>

              {/* Status */}
              <div>
                <label htmlFor="status" className="block text-sm font-medium text-gray-700 mb-2">
                  Status *
                </label>
                <select
                  id="status"
                  name="status"
                  value={formData.status}
                  onChange={handleChange}
                  className="input-field"
                  required
                  disabled={loading}
                >
                  <option value="Lost">Lost</option>
                  <option value="Found">Found</option>
                </select>
              </div>

              {/* Image Upload */}
              <div>
                <label htmlFor="image" className="block text-sm font-medium text-gray-700 mb-2">
                  Upload Image (Optional)
                </label>
                <input
                  type="file"
                  id="image"
                  name="image"
                  onChange={handleImageChange}
                  className="input-field"
                  accept="image/*"
                  disabled={loading}
                />
                {imagePreview && (
                  <div className="mt-2">
                    <img
                      src={imagePreview}
                      alt="Preview"
                      className="w-32 h-32 object-cover rounded-md border border-gray-300"
                    />
                  </div>
                )}
              </div>

              {/* Submit Button */}
              <div className="pt-4">
                <button
                  type="submit"
                  className="btn-success w-full"
                  disabled={loading}
                >
                  {loading ? (
                    <div className="flex items-center justify-center">
                      <div className="animate-spin rounded-full h-5 w-5 border-b-2 border-white mr-2"></div>
                      Submitting...
                    </div>
                  ) : (
                    'Submit Report'
                  )}
                </button>
              </div>
            </form>

            {/* Tips Section */}
            <div className="mt-8 p-4 bg-blue-50 rounded-lg border border-blue-200">
              <h3 className="text-lg font-medium text-blue-900 mb-2">Tips for Better Results</h3>
              <ul className="text-blue-800 text-sm space-y-1">
                <li>• Be specific with the item type and description</li>
                <li>• Include unique identifying features</li>
                <li>• Provide accurate location details</li>
                <li>• Upload a clear image if possible</li>
              </ul>
            </div>
          </div>
        </div>
      </div>

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

export default Report; 