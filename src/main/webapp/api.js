// API Base URL
const API_BASE = '/volunteer-management-system/api';

// Generic API functions
async function apiGet(endpoint) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            headers: {
                'Cache-Control': 'no-cache, no-store, must-revalidate',
                'Pragma': 'no-cache',
                'Expires': '0'
            }
        });
        const data = await response.json();
        if (data.success) {
            return data.data;
        } else {
            throw new Error(data.message || 'Error fetching data');
        }
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

async function apiPost(endpoint, body) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });
        const data = await response.json();
        if (response.ok && data.success) {
            return data.data;
        } else {
            throw new Error(data.message || 'Error creating resource');
        }
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

async function apiPut(endpoint, body) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(body)
        });
        const data = await response.json();
        if (response.ok && data.success) {
            return data.data;
        } else {
            throw new Error(data.message || 'Error updating resource');
        }
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

async function apiDelete(endpoint) {
    try {
        const response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'DELETE'
        });
        const data = await response.json();
        if (response.ok && data.success) {
            return true;
        } else {
            throw new Error(data.message || 'Error deleting resource');
        }
    } catch (error) {
        console.error('API Error:', error);
        throw error;
    }
}

// Specific API functions
const API = {
    // Organizations
    getOrganizations: () => apiGet('/organizations'),
    getOrganization: (id) => apiGet(`/organizations/${id}`),
    createOrganization: (org) => apiPost('/organizations', org),
    updateOrganization: (id, org) => apiPut(`/organizations/${id}`, org),
    deleteOrganization: (id) => apiDelete(`/organizations/${id}`),

    // Volunteers
    getVolunteers: () => apiGet('/volunteers'),
    getVolunteer: (id) => apiGet(`/volunteers/${id}`),
    createVolunteer: (vol) => apiPost('/volunteers', vol),
    updateVolunteer: (id, vol) => apiPut(`/volunteers/${id}`, vol),
    deleteVolunteer: (id) => apiDelete(`/volunteers/${id}`),

    // Projects
    getProjects: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/projects${query ? '?' + query : ''}`);
    },
    getProject: (id) => apiGet(`/projects/${id}`),
    createProject: (proj) => apiPost('/projects', proj),
    updateProject: (id, proj) => apiPut(`/projects/${id}`, proj),
    deleteProject: (id) => apiDelete(`/projects/${id}`),

    // Skills
    getSkills: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/skills${query ? '?' + query : ''}`);
    },
    getSkill: (id) => apiGet(`/skills/${id}`),
    createSkill: (skill) => apiPost('/skills', skill),
    updateSkill: (id, skill) => apiPut(`/skills/${id}`, skill),
    deleteSkill: (id) => apiDelete(`/skills/${id}`),

    // Events
    getEvents: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/events${query ? '?' + query : ''}`);
    },
    getEvent: (id) => apiGet(`/events/${id}`),
    createEvent: (event) => apiPost('/events', event),
    updateEvent: (id, event) => apiPut(`/events/${id}`, event),
    deleteEvent: (id) => apiDelete(`/events/${id}`),

    // Assignments
    getAssignments: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/assignments${query ? '?' + query : ''}`);
    },
    getAssignment: (id) => apiGet(`/assignments/${id}`),
    createAssignment: (assignment) => apiPost('/assignments', assignment),
    updateAssignment: (id, assignment) => apiPut(`/assignments/${id}`, assignment),
    deleteAssignment: (id) => apiDelete(`/assignments/${id}`),

    // Attendance
    getAttendance: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/attendance${query ? '?' + query : ''}`);
    },
    getAttendanceById: (id) => apiGet(`/attendance/${id}`),
    createAttendance: (attendance) => apiPost('/attendance', attendance),
    updateAttendance: (id, attendance) => apiPut(`/attendance/${id}`, attendance),
    deleteAttendance: (id) => apiDelete(`/attendance/${id}`),

    // Certificates
    getCertificates: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/certificates${query ? '?' + query : ''}`);
    },
    getCertificate: (id) => apiGet(`/certificates/${id}`),
    createCertificate: (cert) => apiPost('/certificates', cert),
    updateCertificate: (id, cert) => apiPut(`/certificates/${id}`, cert),
    deleteCertificate: (id) => apiDelete(`/certificates/${id}`),

    // Feedback
    getFeedback: (params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/feedback${query ? '?' + query : ''}`);
    },
    getFeedbackById: (id) => apiGet(`/feedback/${id}`),
    createFeedback: (feedback) => apiPost('/feedback', feedback),
    updateFeedback: (id, feedback) => apiPut(`/feedback/${id}`, feedback),
    deleteFeedback: (id) => apiDelete(`/feedback/${id}`),

    // Statistics
    getStatistics: (type, params = {}) => {
        const query = new URLSearchParams(params).toString();
        return apiGet(`/statistics/${type}${query ? '?' + query : ''}`);
    }
};
