import { clsx } from 'clsx';
import { useState } from 'react';

const ItemCard = ({ item }) => {
  const [showContact, setShowContact] = useState(false);

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric'
    });
  };

  const imageSrc = item.imageBase64 ? `data:image/jpeg;base64,${item.imageBase64}` : item.image;
  const contactInfo = item.contactEmail || item.contact;

  return (
    <div className="card group hover:shadow-xl transition-all duration-300">
      {/* Image */}
      <div className="aspect-w-16 aspect-h-9 mb-4 rounded-md overflow-hidden bg-gray-100">
        {imageSrc ? (
          <img
            src={imageSrc}
            alt={item.itemType}
            className="w-full h-48 object-cover group-hover:scale-105 transition-transform duration-300"
          />
        ) : (
          <div className="w-full h-48 bg-gray-200 flex items-center justify-center">
            <svg className="w-12 h-12 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M4 3a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V5a2 2 0 00-2-2H4zm12 12H4l4-8 3 6 2-4 3 6z" clipRule="evenodd" />
            </svg>
          </div>
        )}
      </div>

      {/* Content */}
      <div className="space-y-4">
        {/* Header */}
        <div className="flex items-start justify-between">
          <h3 className="text-lg font-semibold text-gray-800 line-clamp-2">
            {item.itemType}
          </h3>
          <span className={clsx(
            'status-badge',
            item.status === 'Lost' ? 'status-lost' : 'status-found'
          )}>
            {item.status}
          </span>
        </div>

        {/* Description */}
        {item.description && (
          <p className="text-gray-600 text-sm line-clamp-3">
            {item.description}
          </p>
        )}

        {/* Details */}
        <div className="space-y-3 text-sm text-gray-500">
          {/* Location */}
          <div className="flex items-center">
            <svg className="w-4 h-4 mr-2 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M5.05 4.05a7 7 0 119.9 9.9L10 18.9l-4.95-4.95a7 7 0 010-9.9zM10 11a2 2 0 100-4 2 2 0 000 4z" clipRule="evenodd" />
            </svg>
            <span className="truncate">{item.location}</span>
          </div>

          {/* Date */}
          <div className="flex items-center">
            <svg className="w-4 h-4 mr-2 text-gray-400" fill="currentColor" viewBox="0 0 20 20">
              <path fillRule="evenodd" d="M6 2a1 1 0 00-1 1v1H4a2 2 0 00-2 2v10a2 2 0 002 2h12a2 2 0 002-2V6a2 2 0 00-2-2h-1V3a1 1 0 10-2 0v1H7V3a1 1 0 00-1-1zm0 5a1 1 0 000 2h8a1 1 0 100-2H6z" clipRule="evenodd" />
            </svg>
            <span>{formatDate(item.date)}</span>
          </div>

          {/* Contact - shown conditionally when button is clicked */}
          {showContact && contactInfo && (
            <div className="flex items-center mt-2 p-2 bg-blue-50 text-blue-700 rounded border border-blue-100">
              <svg className="w-4 h-4 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path d="M2.003 5.884L10 9.882l7.997-3.998A2 2 0 0016 4H4a2 2 0 00-1.997 1.884z" />
                <path d="M18 8.118l-8 4-8-4V14a2 2 0 002 2h12a2 2 0 002-2V8.118z" />
              </svg>
              <span className="font-medium break-all">{contactInfo}</span>
            </div>
          )}
        </div>

        {/* Actions */}
        <div className="flex space-x-2 pt-3 border-t border-gray-100">
          <button 
            className="btn-primary text-xs py-2 px-3 flex-1"
            onClick={() => setShowContact(!showContact)}
          >
            {showContact ? 'Hide Contact' : `Contact ${item.status === 'Lost' ? 'Owner' : 'Finder'}`}
          </button>
        </div>
      </div>
    </div>
  );
};

export default ItemCard; 