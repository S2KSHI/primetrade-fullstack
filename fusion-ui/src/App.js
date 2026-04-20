import React, { useState, useEffect } from 'react';
import './App.css';

function App() {
  const [isLogin, setIsLogin] = useState(true);
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [token, setToken] = useState('');
  const [tasks, setTasks] = useState([]);
  const [darkMode, setDarkMode] = useState(false);
  
  // Form states
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [name, setName] = useState('');
  const [taskTitle, setTaskTitle] = useState('');
  const [taskDesc, setTaskDesc] = useState('');
  const [showPassword, setShowPassword] = useState(false);
  const [userName, setUserName] = useState('');

const API_URL = 'https://primetrade-fullstack.onrender.com/api/v1';

  // Check theme and login status on mount
  useEffect(() => {
    const savedTheme = localStorage.getItem('theme');
    if (savedTheme === 'dark') {
      setDarkMode(true);
      document.body.classList.add('dark-mode');
    }
    
    const savedToken = localStorage.getItem('token');
    const savedName = localStorage.getItem('userName');
    if (savedToken) {
      setToken(savedToken);
      setUserName(savedName || 'User');
      setIsAuthenticated(true);
      fetchTasks(savedToken);
    }
  }, []);

  // Toggle dark mode
  const toggleDarkMode = () => {
    setDarkMode(!darkMode);
    document.body.classList.toggle('dark-mode');
    localStorage.setItem('theme', !darkMode ? 'dark' : 'light');
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(`${API_URL}/auth/register`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ name, email, password })
      });
      
      if (res.ok) {
        alert('✅ Registration successful! Please login.');
        setIsLogin(true);
        setName('');
        setEmail('');
        setPassword('');
      } else {
        const error = await res.text();
        alert('❌ ' + error);
      }
    } catch (err) {
      alert('❌ Registration failed');
    }
  };

  const handleLogin = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(`${API_URL}/auth/login`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ email, password })
      });
      
      if (res.ok) {
        const data = await res.json();
        setToken(data.token);
        setUserName(name || email.split('@')[0]);
        localStorage.setItem('token', data.token);
        localStorage.setItem('userName', name || email.split('@')[0]);
        setIsAuthenticated(true);
        fetchTasks(data.token);
        setEmail('');
        setPassword('');
      } else {
        alert('❌ Invalid credentials');
      }
    } catch (err) {
      alert('❌ Login failed');
    }
  };

  const fetchTasks = async (authToken) => {
    try {
      const res = await fetch(`${API_URL}/tasks`, {
        headers: { 'Authorization': `Bearer ${authToken}` }
      });
      const data = await res.json();
      setTasks(data);
    } catch (err) {
      console.error('Failed to fetch tasks');
    }
  };

  const handleCreateTask = async (e) => {
    e.preventDefault();
    try {
      const res = await fetch(`${API_URL}/tasks`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ title: taskTitle, description: taskDesc })
      });
      
      if (res.ok) {
        setTaskTitle('');
        setTaskDesc('');
        fetchTasks(token);
      }
    } catch (err) {
      alert('Failed to create task');
    }
  };

  const handleUpdateTask = async (id, status) => {
    try {
      const task = tasks.find(t => t.id === id);
      await fetch(`${API_URL}/tasks/${id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${token}`
        },
        body: JSON.stringify({ ...task, status })
      });
      fetchTasks(token);
    } catch (err) {
      alert('Failed to update task');
    }
  };

  const handleDeleteTask = async (id) => {
    try {
      await fetch(`${API_URL}/tasks/${id}`, {
        method: 'DELETE',
        headers: { 'Authorization': `Bearer ${token}` }
      });
      fetchTasks(token);
    } catch (err) {
      alert('Failed to delete task');
    }
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('userName');
    setIsAuthenticated(false);
    setToken('');
    setTasks([]);
    setUserName('');
  };

  const getGreeting = () => {
    const hour = new Date().getHours();
    if (hour < 12) return '🌅 Good Morning';
    if (hour < 18) return '☀️ Good Afternoon';
    return '🌙 Good Evening';
  };

  if (!isAuthenticated) {
    return (
      <div className="auth-container">
        <button className="theme-toggle" onClick={toggleDarkMode}>
          {darkMode ? '☀️' : '🌙'}
        </button>
        
        <div className="auth-box">
          <div className="logo">
            <div className="logo-icon">PT</div>
            <h1>PrimeTrade</h1>
            <p className="tagline">Manage your tasks efficiently</p>
          </div>
          
          <div className="auth-tabs">
            <button 
              className={isLogin ? 'active' : ''} 
              onClick={() => setIsLogin(true)}
            >
              Login
            </button>
            <button 
              className={!isLogin ? 'active' : ''} 
              onClick={() => setIsLogin(false)}
            >
              Register
            </button>
          </div>

          {isLogin ? (
            <form onSubmit={handleLogin} className="auth-form">
              <div className="input-group">
                <label>Email</label>
                <input 
                  type="email" 
                  placeholder="Enter your email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required 
                />
              </div>
              
              <div className="input-group">
                <label>Password</label>
                <div className="password-wrapper">
                  <input 
                    type={showPassword ? "text" : "password"}
                    placeholder="Enter your password" 
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required 
                  />
                  <button 
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? '👁️' : '👁️‍🗨️'}
                  </button>
                </div>
              </div>
              
              <button type="submit" className="btn-primary">
                Login →
              </button>
            </form>
          ) : (
            <form onSubmit={handleRegister} className="auth-form">
              <div className="input-group">
                <label>Full Name</label>
                <input 
                  type="text" 
                  placeholder="Enter your name" 
                  value={name}
                  onChange={(e) => setName(e.target.value)}
                  required 
                />
              </div>
              
              <div className="input-group">
                <label>Email</label>
                <input 
                  type="email" 
                  placeholder="Enter your email" 
                  value={email}
                  onChange={(e) => setEmail(e.target.value)}
                  required 
                />
              </div>
              
              <div className="input-group">
                <label>Password</label>
                <div className="password-wrapper">
                  <input 
                    type={showPassword ? "text" : "password"}
                    placeholder="Create a password" 
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    required 
                  />
                  <button 
                    type="button"
                    className="password-toggle"
                    onClick={() => setShowPassword(!showPassword)}
                  >
                    {showPassword ? '👁️' : '👁️‍🗨️'}
                  </button>
                </div>
              </div>
              
              <button type="submit" className="btn-primary">
                Create Account →
              </button>
            </form>
          )}
        </div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <nav className="navbar">
        <div className="nav-left">
          <div className="logo-icon small">PT</div>
          <div className="nav-info">
            <span className="greeting">{getGreeting()}</span>
            <span className="username">{userName}</span>
          </div>
        </div>
        
        <div className="nav-right">
          <button className="theme-toggle-nav" onClick={toggleDarkMode}>
            {darkMode ? '☀️ Light' : '🌙 Dark'}
          </button>
          <button onClick={logout} className="btn-logout">
            Logout
          </button>
        </div>
      </nav>

      <div className="dashboard-content">
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon">📋</div>
            <div className="stat-info">
              <h3>{tasks.length}</h3>
              <p>Total Tasks</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">⏳</div>
            <div className="stat-info">
              <h3>{tasks.filter(t => t.status === 'PENDING').length}</h3>
              <p>Pending</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">🚀</div>
            <div className="stat-info">
              <h3>{tasks.filter(t => t.status === 'IN_PROGRESS').length}</h3>
              <p>In Progress</p>
            </div>
          </div>
          
          <div className="stat-card">
            <div className="stat-icon">✅</div>
            <div className="stat-info">
              <h3>{tasks.filter(t => t.status === 'COMPLETED').length}</h3>
              <p>Completed</p>
            </div>
          </div>
        </div>

        <div className="task-form-section">
          <h2>✨ Create New Task</h2>
          <form onSubmit={handleCreateTask} className="task-form">
            <input 
              type="text" 
              placeholder="Task Title" 
              value={taskTitle}
              onChange={(e) => setTaskTitle(e.target.value)}
              required 
            />
            <textarea 
              placeholder="Task Description" 
              value={taskDesc}
              onChange={(e) => setTaskDesc(e.target.value)}
              rows="3"
              required
            />
            <button type="submit" className="btn-create">
              Create Task +
            </button>
          </form>
        </div>

        <div className="tasks-section">
          <h2>📝 Your Tasks</h2>
          
          {tasks.length === 0 ? (
            <div className="empty-state">
              <div className="empty-icon">📭</div>
              <p>No tasks yet. Create your first task above!</p>
            </div>
          ) : (
            <div className="tasks-grid">
              {tasks.map(task => (
                <div key={task.id} className={`task-card status-${task.status.toLowerCase()}`}>
                  <div className="task-header">
                    <h3>{task.title}</h3>
                    <span className={`badge badge-${task.status.toLowerCase()}`}>
                      {task.status === 'PENDING' && '⏳'}
                      {task.status === 'IN_PROGRESS' && '🚀'}
                      {task.status === 'COMPLETED' && '✅'}
                      {' '}
                      {task.status.replace('_', ' ')}
                    </span>
                  </div>
                  <p className="task-description">{task.description}</p>
                  
                  <div className="task-actions">
                    <select 
                      value={task.status} 
                      onChange={(e) => handleUpdateTask(task.id, e.target.value)}
                      className="status-select"
                    >
                      <option value="PENDING">Pending</option>
                      <option value="IN_PROGRESS">In Progress</option>
                      <option value="COMPLETED">Completed</option>
                    </select>
                    <button 
                      onClick={() => handleDeleteTask(task.id)} 
                      className="btn-delete"
                    >
                      🗑️ Delete
                    </button>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}

export default App;