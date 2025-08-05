import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import ItemCard from '../components/ItemCard';

const SearchResults = () => {
  const [matchedItems, setMatchedItems] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // Get matched items from localStorage
    const storedItems = localStorage.getItem('matchedItems');
    if (storedItems) {
      try {
        const items = JSON.parse(storedItems);
        setMatchedItems(items);
      } catch (error) {
        console.error('Error parsing matched items:', error);
      }
    }
    setLoading(false);
  }, []);

  const clearResults = () => {
    localStorage.removeItem('matchedItems');
    setMatchedItems([]);
  };

  if (loading) {
    return (
      <div className="page-container">
        <div className="main-content">
          <div className="text-center">
            <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600 mx-auto"></div>
            <p className="mt-4 text-gray-600">Loading results...</p>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="page-container">
      <div className="main-content">
        {/* Header */}
        <div className="mb-8">
          <div className="flex justify-between items-center mb-4">
            <h1 className="text-heading">Search Results</h1>
            <button
              onClick={clearResults}
              className="btn-secondary"
            >
              Clear Results
            </button>
          </div>
          
          {matchedItems.length > 0 ? (
            <p className="text-subheading">
              Found {matchedItems.length} similar item{matchedItems.length !== 1 ? 's' : ''} that might match your report.
            </p>
          ) : (
            <p className="text-subheading">
              No similar items found. Your report has been submitted successfully.
            </p>
          )}
        </div>

        {/* Results */}
        {matchedItems.length > 0 ? (
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-6">
            {matchedItems.map((item) => (
              <ItemCard key={item.id} item={item} />
            ))}
          </div>
        ) : (
          <div className="text-center py-12">
            <div className="w-24 h-24 bg-gray-100 rounded-full flex items-center justify-center mx-auto mb-4 shadow-sm">
              <svg className="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M8 4a4 4 0 100 8 4 4 0 000-8zM2 8a6 6 0 1110.89 3.476l4.817 4.817a1 1 0 01-1.414 1.414l-4.816-4.816A6 6 0 012 8z" clipRule="evenodd" />
              </svg>
            </div>
            <h3 className="text-lg font-medium text-gray-800 mb-2">No Matches Found</h3>
            <p className="text-gray-600 mb-6">
              We couldn't find any similar items in our database. Your report has been saved and will be visible to others searching for similar items.
            </p>
            <div className="space-y-4">
              <Link to="/" className="btn-primary">
                Back to Home
              </Link>
              <Link to="/report" className="btn-secondary block">
                Report Another Item
              </Link>
            </div>
          </div>
        )}

        {/* Actions */}
        {matchedItems.length > 0 && (
          <div className="mt-8 text-center">
            <div className="bg-blue-50 rounded-lg p-6 border border-blue-200">
              <h3 className="text-lg font-medium text-blue-900 mb-2">What's Next?</h3>
              <p className="text-blue-800 mb-4">
                Review the matched items above. If you find your item, contact the owner directly. 
                If not, your report will remain active and visible to others.
              </p>
              <div className="flex flex-col sm:flex-row gap-4 justify-center">
                <Link to="/" className="btn-primary">
                  Back to Home
                </Link>
                <Link to="/report" className="btn-secondary">
                  Report Another Item
                </Link>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default SearchResults; 