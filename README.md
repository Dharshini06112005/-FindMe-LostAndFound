# FindMe - Lost and Found Application

A modern web application for reporting and finding lost items, built with React and Spring Boot.

## 🌟 Features

- **User Authentication:** Sign up and login functionality
- **Report Items:** Submit lost or found items with details and images
- **Search & Match:** Intelligent matching system for similar items
- **User Profiles:** View and manage your submitted reports
- **Responsive Design:** Works on desktop, tablet, and mobile

## 🛠️ Tech Stack

### Frontend
- **React 18** with Vite
- **Tailwind CSS** for styling
- **React Router** for navigation
- **Axios** for API calls
- **LocalStorage** for token management

### Backend
- **Spring Boot 3.5.4**
- **Spring Data JPA** for database operations
- **PostgreSQL** database
- **Spring Security** with CORS configuration
- **Gemini AI** integration for intelligent matching

## 📁 Project Structure

```
Findme/
├── frontend/                 # React application
│   ├── src/
│   │   ├── components/      # Reusable components
│   │   ├── pages/          # Page components
│   │   ├── context/        # React context
│   │   └── ...
│   └── package.json
├── backend/                 # Spring Boot application
│   └── demo/
│       ├── src/main/java/
│       │   ├── controller/ # REST controllers
│       │   ├── service/    # Business logic
│       │   ├── entity/     # JPA entities
│       │   └── ...
│       └── pom.xml
└── README.md
```

## 🚀 Getting Started

### Prerequisites
- Node.js (v16 or higher)
- Java 17 or higher
- PostgreSQL database
- Maven

### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/Dharshini06112005/-FindMe-LostAndFound.git
   cd -FindMe-LostAndFound
   ```

2. **Setup Backend**
   ```bash
   cd backend/demo
   mvn spring-boot:run
   ```
   Backend will run on: http://localhost:8080

3. **Setup Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
   Frontend will run on: http://localhost:5173

4. **Access the application**
   Open http://localhost:5173 in your browser

## ⚙️ Configuration

### Database Setup
1. Create PostgreSQL database named `findme_db`
2. Update `backend/demo/src/main/resources/application.properties` with your database credentials

### API Keys
- Add your Gemini API key in `application.properties`

## 🎯 Features

- **Home Page:** Welcome screen with recent items
- **Authentication:** Login and signup pages
- **Report Items:** Form to submit lost/found items
- **Search Results:** Display matched items
- **User Profile:** View your submitted reports
- **Responsive Design:** Mobile-friendly interface

## 🔧 API Endpoints

### Authentication
- `POST /api/auth/signup` - User registration
- `POST /api/auth/login` - User login

### Reports
- `POST /api/report` - Submit lost/found item
- `GET /api/recent` - Get recent items
- `GET /api/user/reports` - Get user's reports

### Lost Items
- `GET /api/lost-items` - Get all lost items
- `POST /api/lost-items` - Create lost item
- `GET /api/lost-items/search` - Search lost items

### AI Integration
- `POST /api/ai/chat` - Chat with Gemini AI
- `GET /api/ai/test` - Test AI endpoint

## 🎨 UI Components

- **Navbar:** Responsive navigation with authentication
- **ItemCard:** Display lost/found items
- **Toast:** Success/error notifications
- **Forms:** Login, signup, and report forms
- **Loading States:** Spinners and progress indicators

## 🔒 Security Features

- **CORS Configuration:** Cross-origin requests enabled
- **Form Validation:** Client and server-side validation
- **Protected Routes:** Authentication required for certain pages
- **Token Management:** JWT tokens stored in localStorage

## 📱 Responsive Design

- **Mobile First:** Optimized for mobile devices
- **Tablet Support:** Responsive layout for tablets
- **Desktop Experience:** Full-featured desktop interface
- **Touch Friendly:** Optimized for touch interactions

## 🚀 Deployment

### Frontend Deployment
```bash
cd frontend
npm run build
# Deploy dist/ folder to your hosting service
```

### Backend Deployment
```bash
cd backend/demo
mvn clean package
# Deploy the generated JAR file
```

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## 📄 License

This project is licensed under the MIT License.

## 👨‍💻 Author

**Dharshini** - [GitHub Profile](https://github.com/Dharshini06112005)

---

**FindMe** - Connecting people with their lost items! 🔍✨

## 🔗 Links

- **Repository:** https://github.com/Dharshini06112005/-FindMe-LostAndFound
- **Live Demo:** Coming Soon
- **Documentation:** This README

---

*Built with ❤️ using React + Spring Boot* 