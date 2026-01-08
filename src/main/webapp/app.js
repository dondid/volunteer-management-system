// Global state
let organizations = [];
let volunteers = [];
let projects = [];
let skills = [];
let events = [];
let assignments = [];
let attendance = [];
let certificates = [];
let feedback = [];

// Initialize on page load
document.addEventListener('DOMContentLoaded', function() {
    // Load data when tabs are shown
    const tabButtons = document.querySelectorAll('[data-bs-toggle="tab"]');
    tabButtons.forEach(button => {
        button.addEventListener('shown.bs.tab', function(event) {
            const target = event.target.getAttribute('data-bs-target');
            if (target === '#organizations') loadOrganizations();
            else if (target === '#volunteers') loadVolunteers();
            else if (target === '#projects') loadProjects();
            else if (target === '#skills') loadSkills();
            else if (target === '#events') loadEvents();
            else if (target === '#assignments') loadAssignments();
            else if (target === '#attendance') loadAttendance();
            else if (target === '#certificates') loadCertificates();
            else if (target === '#feedback') loadFeedback();
        });
    });
    
    // Load initial data
    loadOrganizations();
});

// ========== ORGANIZATIONS ==========
async function loadOrganizations() {
    try {
        organizations = await API.getOrganizations();
        displayOrganizations();
    } catch (error) {
        showError('organizations-list', error.message);
    }
}

function displayOrganizations() {
    const container = document.getElementById('organizations-list');
    if (!organizations || organizations.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-building"></i><p>Nu există organizații</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nume</th>
                        <th>Email</th>
                        <th>Telefon</th>
                        <th>Status</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${organizations.map(org => `
                        <tr>
                            <td>${org.id}</td>
                            <td>${org.name}</td>
                            <td>${org.email}</td>
                            <td>${org.phone || '-'}</td>
                            <td><span class="badge bg-${org.status === 'ACTIVE' ? 'success' : 'secondary'}">${org.status}</span></td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editOrganization(${org.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteOrganization(${org.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showOrganizationForm(id = null) {
    const org = id ? organizations.find(o => o.id === id) : null;
    const modalBody = `
        <form id="organization-form">
            <input type="hidden" id="org-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Nume *</label>
                <input type="text" class="form-control" id="org-name" value="${org?.name || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Email *</label>
                <input type="email" class="form-control" id="org-email" value="${org?.email || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Telefon</label>
                <input type="text" class="form-control" id="org-phone" value="${org?.phone || ''}">
            </div>
            <div class="mb-3">
                <label class="form-label">Adresă</label>
                <textarea class="form-control" id="org-address">${org?.address || ''}</textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">Status</label>
                <select class="form-select" id="org-status">
                    <option value="ACTIVE" ${org?.status === 'ACTIVE' ? 'selected' : ''}>Active</option>
                    <option value="INACTIVE" ${org?.status === 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                    <option value="PENDING" ${org?.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                </select>
            </div>
        </form>
    `;
    
    createModal('organization-modal', 'Organizație', modalBody, async () => {
        const formData = {
            name: document.getElementById('org-name').value,
            email: document.getElementById('org-email').value,
            phone: document.getElementById('org-phone').value,
            address: document.getElementById('org-address').value,
            status: document.getElementById('org-status').value
        };
        
        try {
            if (id) {
                await API.updateOrganization(id, formData);
            } else {
                await API.createOrganization(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('organization-modal')).hide();
            loadOrganizations();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('organization-modal')).show();
}

async function editOrganization(id) {
    showOrganizationForm(id);
}

async function deleteOrganization(id) {
    if (!confirm('Ești sigur că vrei să ștergi această organizație?')) return;
    try {
        await API.deleteOrganization(id);
        loadOrganizations();
        showSuccess('Organizație ștearsă!');
    } catch (error) {
        showError('organizations-list', error.message);
    }
}

// ========== VOLUNTEERS ==========
async function loadVolunteers() {
    try {
        volunteers = await API.getVolunteers();
        displayVolunteers();
    } catch (error) {
        showError('volunteers-list', error.message);
    }
}

function displayVolunteers() {
    const container = document.getElementById('volunteers-list');
    if (!volunteers || volunteers.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-person-badge"></i><p>Nu există voluntari</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nume</th>
                        <th>Email</th>
                        <th>Telefon</th>
                        <th>Status</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${volunteers.map(vol => `
                        <tr>
                            <td>${vol.id}</td>
                            <td>${vol.firstName} ${vol.lastName}</td>
                            <td>${vol.email}</td>
                            <td>${vol.phone || '-'}</td>
                            <td><span class="badge bg-${vol.status === 'ACTIVE' ? 'success' : 'secondary'}">${vol.status}</span></td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editVolunteer(${vol.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteVolunteer(${vol.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showVolunteerForm(id = null) {
    const vol = id ? volunteers.find(v => v.id === id) : null;
    const modal = createModal('volunteer-modal', 'Voluntar', `
        <form id="volunteer-form">
            <input type="hidden" id="vol-id" value="${id || ''}">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Prenume *</label>
                    <input type="text" class="form-control" id="vol-firstname" value="${vol?.firstName || ''}" required>
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Nume *</label>
                    <input type="text" class="form-control" id="vol-lastname" value="${vol?.lastName || ''}" required>
                </div>
            </div>
            <div class="mb-3">
                <label class="form-label">Email *</label>
                <input type="email" class="form-control" id="vol-email" value="${vol?.email || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Telefon</label>
                <input type="text" class="form-control" id="vol-phone" value="${vol?.phone || ''}">
            </div>
            <div class="mb-3">
                <label class="form-label">Status</label>
                <select class="form-select" id="vol-status">
                    <option value="ACTIVE" ${vol?.status === 'ACTIVE' ? 'selected' : ''}>Active</option>
                    <option value="INACTIVE" ${vol?.status === 'INACTIVE' ? 'selected' : ''}>Inactive</option>
                    <option value="BLOCKED" ${vol?.status === 'BLOCKED' ? 'selected' : ''}>Blocked</option>
                </select>
            </div>
        </form>
    `, async () => {
        const formData = {
            firstName: document.getElementById('vol-firstname').value,
            lastName: document.getElementById('vol-lastname').value,
            email: document.getElementById('vol-email').value,
            phone: document.getElementById('vol-phone').value,
            status: document.getElementById('vol-status').value
        };
        
        try {
            if (id) {
                await API.updateVolunteer(id, formData);
            } else {
                await API.createVolunteer(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('volunteer-modal')).hide();
            loadVolunteers();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('volunteer-modal')).show();
}

async function editVolunteer(id) {
    showVolunteerForm(id);
}

async function deleteVolunteer(id) {
    if (!confirm('Ești sigur că vrei să ștergi acest voluntar?')) return;
    try {
        await API.deleteVolunteer(id);
        loadVolunteers();
        showSuccess('Voluntar șters!');
    } catch (error) {
        showError('volunteers-list', error.message);
    }
}

// ========== PROJECTS ==========
async function loadProjects() {
    try {
        const filter = document.getElementById('project-filter')?.value;
        const params = {};
        if (filter === 'available') params.available = true;
        else if (filter) params.status = filter;
        
        projects = await API.getProjects(params);
        displayProjects();
    } catch (error) {
        showError('projects-list', error.message);
    }
}

function displayProjects() {
    const container = document.getElementById('projects-list');
    if (!projects || projects.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-folder"></i><p>Nu există proiecte</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Titlu</th>
                        <th>Organizație</th>
                        <th>Status</th>
                        <th>Voluntari</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${projects.map(proj => `
                        <tr>
                            <td>${proj.id}</td>
                            <td>${proj.title}</td>
                            <td>${proj.organization?.name || '-'}</td>
                            <td><span class="badge bg-${getStatusColor(proj.status)}">${proj.status}</span></td>
                            <td>${proj.currentVolunteers || 0}/${proj.maxVolunteers || 0}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editProject(${proj.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteProject(${proj.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showProjectForm(id = null) {
    const proj = id ? projects.find(p => p.id === id) : null;
    const modal = createModal('project-modal', 'Proiect', `
        <form id="project-form">
            <input type="hidden" id="proj-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Titlu *</label>
                <input type="text" class="form-control" id="proj-title" value="${proj?.title || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Organizație ID *</label>
                <input type="number" class="form-control" id="proj-org-id" value="${proj?.organization?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Status</label>
                <select class="form-select" id="proj-status">
                    <option value="DRAFT" ${proj?.status === 'DRAFT' ? 'selected' : ''}>Draft</option>
                    <option value="ACTIVE" ${proj?.status === 'ACTIVE' ? 'selected' : ''}>Active</option>
                    <option value="COMPLETED" ${proj?.status === 'COMPLETED' ? 'selected' : ''}>Completed</option>
                    <option value="CANCELLED" ${proj?.status === 'CANCELLED' ? 'selected' : ''}>Cancelled</option>
                </select>
            </div>
            <div class="row">
                <div class="col-md-6 mb-3">
                    <label class="form-label">Voluntari Max</label>
                    <input type="number" class="form-control" id="proj-max-vol" value="${proj?.maxVolunteers || 10}">
                </div>
                <div class="col-md-6 mb-3">
                    <label class="form-label">Data Start</label>
                    <input type="date" class="form-control" id="proj-start-date" value="${proj?.startDate || ''}">
                </div>
            </div>
        </form>
    `, async () => {
        const formData = {
            title: document.getElementById('proj-title').value,
            organization: { id: parseInt(document.getElementById('proj-org-id').value) },
            status: document.getElementById('proj-status').value,
            maxVolunteers: parseInt(document.getElementById('proj-max-vol').value) || 10,
            startDate: document.getElementById('proj-start-date').value
        };
        
        try {
            if (id) {
                await API.updateProject(id, formData);
            } else {
                await API.createProject(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('project-modal')).hide();
            loadProjects();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('project-modal')).show();
}

async function editProject(id) {
    showProjectForm(id);
}

async function deleteProject(id) {
    if (!confirm('Ești sigur că vrei să ștergi acest proiect?')) return;
    try {
        await API.deleteProject(id);
        loadProjects();
        showSuccess('Proiect șters!');
    } catch (error) {
        showError('projects-list', error.message);
    }
}

// ========== SKILLS ==========
async function loadSkills() {
    try {
        skills = await API.getSkills();
        displaySkills();
    } catch (error) {
        showError('skills-list', error.message);
    }
}

function displaySkills() {
    const container = document.getElementById('skills-list');
    if (!skills || skills.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-star"></i><p>Nu există competențe</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Nume</th>
                        <th>Categorie</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${skills.map(skill => `
                        <tr>
                            <td>${skill.id}</td>
                            <td>${skill.name}</td>
                            <td><span class="badge bg-info">${skill.category}</span></td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editSkill(${skill.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteSkill(${skill.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showSkillForm(id = null) {
    const skill = id ? skills.find(s => s.id === id) : null;
    const modal = createModal('skill-modal', 'Competență', `
        <form id="skill-form">
            <input type="hidden" id="skill-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Nume *</label>
                <input type="text" class="form-control" id="skill-name" value="${skill?.name || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Categorie</label>
                <select class="form-select" id="skill-category">
                    <option value="TECHNICAL" ${skill?.category === 'TECHNICAL' ? 'selected' : ''}>Technical</option>
                    <option value="SOCIAL" ${skill?.category === 'SOCIAL' ? 'selected' : ''}>Social</option>
                    <option value="MANAGEMENT" ${skill?.category === 'MANAGEMENT' ? 'selected' : ''}>Management</option>
                    <option value="CREATIVE" ${skill?.category === 'CREATIVE' ? 'selected' : ''}>Creative</option>
                    <option value="LANGUAGE" ${skill?.category === 'LANGUAGE' ? 'selected' : ''}>Language</option>
                    <option value="OTHER" ${skill?.category === 'OTHER' ? 'selected' : ''}>Other</option>
                </select>
            </div>
        </form>
    `, async () => {
        const formData = {
            name: document.getElementById('skill-name').value,
            category: document.getElementById('skill-category').value
        };
        
        try {
            if (id) {
                await API.updateSkill(id, formData);
            } else {
                await API.createSkill(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('skill-modal')).hide();
            loadSkills();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('skill-modal')).show();
}

async function editSkill(id) {
    showSkillForm(id);
}

async function deleteSkill(id) {
    if (!confirm('Ești sigur că vrei să ștergi această competență?')) return;
    try {
        await API.deleteSkill(id);
        loadSkills();
        showSuccess('Competență ștearsă!');
    } catch (error) {
        showError('skills-list', error.message);
    }
}

// ========== EVENTS ==========
async function loadEvents() {
    try {
        const filter = document.getElementById('event-filter')?.value;
        const params = {};
        if (filter === 'upcoming') params.upcoming = true;
        else if (filter === 'available') params.available = true;
        
        events = await API.getEvents(params);
        displayEvents();
    } catch (error) {
        showError('events-list', error.message);
    }
}

function displayEvents() {
    const container = document.getElementById('events-list');
    if (!events || events.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-calendar-event"></i><p>Nu există evenimente</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Titlu</th>
                        <th>Data</th>
                        <th>Status</th>
                        <th>Participanți</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${events.map(event => `
                        <tr>
                            <td>${event.id}</td>
                            <td>${event.title}</td>
                            <td>${formatDateTime(event.eventDate)}</td>
                            <td><span class="badge bg-${getStatusColor(event.status)}">${event.status}</span></td>
                            <td>${event.currentParticipants || 0}/${event.maxParticipants || 0}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editEvent(${event.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteEvent(${event.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showEventForm(id = null) {
    const event = id ? events.find(e => e.id === id) : null;
    const modal = createModal('event-modal', 'Eveniment', `
        <form id="event-form">
            <input type="hidden" id="event-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Titlu *</label>
                <input type="text" class="form-control" id="event-title" value="${event?.title || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Proiect ID *</label>
                <input type="number" class="form-control" id="event-project-id" value="${event?.project?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Data Eveniment *</label>
                <input type="datetime-local" class="form-control" id="event-date" value="${formatDateTimeForInput(event?.eventDate)}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Participanți Max</label>
                <input type="number" class="form-control" id="event-max-participants" value="${event?.maxParticipants || 50}">
            </div>
        </form>
    `, async () => {
        const formData = {
            title: document.getElementById('event-title').value,
            project: { id: parseInt(document.getElementById('event-project-id').value) },
            eventDate: document.getElementById('event-date').value,
            maxParticipants: parseInt(document.getElementById('event-max-participants').value) || 50
        };
        
        try {
            if (id) {
                await API.updateEvent(id, formData);
            } else {
                await API.createEvent(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('event-modal')).hide();
            loadEvents();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('event-modal')).show();
}

async function editEvent(id) {
    showEventForm(id);
}

async function deleteEvent(id) {
    if (!confirm('Ești sigur că vrei să ștergi acest eveniment?')) return;
    try {
        await API.deleteEvent(id);
        loadEvents();
        showSuccess('Eveniment șters!');
    } catch (error) {
        showError('events-list', error.message);
    }
}

// ========== ASSIGNMENTS ==========
async function loadAssignments() {
    try {
        assignments = await API.getAssignments();
        displayAssignments();
    } catch (error) {
        showError('assignments-list', error.message);
    }
}

function displayAssignments() {
    const container = document.getElementById('assignments-list');
    if (!assignments || assignments.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-link-45deg"></i><p>Nu există asignări</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Voluntar</th>
                        <th>Proiect</th>
                        <th>Status</th>
                        <th>Data</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${assignments.map(ass => `
                        <tr>
                            <td>${ass.id}</td>
                            <td>${ass.volunteer?.firstName || ''} ${ass.volunteer?.lastName || ''}</td>
                            <td>${ass.project?.title || '-'}</td>
                            <td><span class="badge bg-${getStatusColor(ass.status)}">${ass.status}</span></td>
                            <td>${ass.assignmentDate || '-'}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editAssignment(${ass.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteAssignment(${ass.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showAssignmentForm(id = null) {
    const ass = id ? assignments.find(a => a.id === id) : null;
    const modal = createModal('assignment-modal', 'Asignare', `
        <form id="assignment-form">
            <input type="hidden" id="ass-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Voluntar ID *</label>
                <input type="number" class="form-control" id="ass-volunteer-id" value="${ass?.volunteer?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Proiect ID *</label>
                <input type="number" class="form-control" id="ass-project-id" value="${ass?.project?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Status</label>
                <select class="form-select" id="ass-status">
                    <option value="PENDING" ${ass?.status === 'PENDING' ? 'selected' : ''}>Pending</option>
                    <option value="ACCEPTED" ${ass?.status === 'ACCEPTED' ? 'selected' : ''}>Accepted</option>
                    <option value="REJECTED" ${ass?.status === 'REJECTED' ? 'selected' : ''}>Rejected</option>
                    <option value="COMPLETED" ${ass?.status === 'COMPLETED' ? 'selected' : ''}>Completed</option>
                </select>
            </div>
            <div class="mb-3">
                <label class="form-label">Data Asignare</label>
                <input type="date" class="form-control" id="ass-date" value="${ass?.assignmentDate || ''}">
            </div>
        </form>
    `, async () => {
        const formData = {
            volunteer: { id: parseInt(document.getElementById('ass-volunteer-id').value) },
            project: { id: parseInt(document.getElementById('ass-project-id').value) },
            status: document.getElementById('ass-status').value,
            assignmentDate: document.getElementById('ass-date').value || new Date().toISOString().split('T')[0]
        };
        
        try {
            if (id) {
                await API.updateAssignment(id, formData);
            } else {
                await API.createAssignment(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('assignment-modal')).hide();
            loadAssignments();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('assignment-modal')).show();
}

async function editAssignment(id) {
    showAssignmentForm(id);
}

async function deleteAssignment(id) {
    if (!confirm('Ești sigur că vrei să ștergi această asignare?')) return;
    try {
        await API.deleteAssignment(id);
        loadAssignments();
        showSuccess('Asignare ștearsă!');
    } catch (error) {
        showError('assignments-list', error.message);
    }
}

// ========== ATTENDANCE ==========
async function loadAttendance() {
    try {
        attendance = await API.getAttendance();
        displayAttendance();
    } catch (error) {
        showError('attendance-list', error.message);
    }
}

function displayAttendance() {
    const container = document.getElementById('attendance-list');
    if (!attendance || attendance.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-clock-history"></i><p>Nu există înregistrări de prezență</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Asignare ID</th>
                        <th>Data</th>
                        <th>Ore Lucrate</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${attendance.map(att => `
                        <tr>
                            <td>${att.id}</td>
                            <td>${att.assignment?.id || '-'}</td>
                            <td>${att.date || '-'}</td>
                            <td>${att.hoursWorked || 0}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editAttendance(${att.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteAttendance(${att.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showAttendanceForm(id = null) {
    const att = id ? attendance.find(a => a.id === id) : null;
    const modal = createModal('attendance-modal', 'Prezență', `
        <form id="attendance-form">
            <input type="hidden" id="att-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Asignare ID *</label>
                <input type="number" class="form-control" id="att-assignment-id" value="${att?.assignment?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Data *</label>
                <input type="date" class="form-control" id="att-date" value="${att?.date || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Ore Lucrate *</label>
                <input type="number" step="0.5" class="form-control" id="att-hours" value="${att?.hoursWorked || 0}" required>
            </div>
        </form>
    `, async () => {
        const formData = {
            assignment: { id: parseInt(document.getElementById('att-assignment-id').value) },
            date: document.getElementById('att-date').value,
            hoursWorked: parseFloat(document.getElementById('att-hours').value)
        };
        
        try {
            if (id) {
                await API.updateAttendance(id, formData);
            } else {
                await API.createAttendance(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('attendance-modal')).hide();
            loadAttendance();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('attendance-modal')).show();
}

async function editAttendance(id) {
    showAttendanceForm(id);
}

async function deleteAttendance(id) {
    if (!confirm('Ești sigur că vrei să ștergi această înregistrare?')) return;
    try {
        await API.deleteAttendance(id);
        loadAttendance();
        showSuccess('Înregistrare ștearsă!');
    } catch (error) {
        showError('attendance-list', error.message);
    }
}

// ========== CERTIFICATES ==========
async function loadCertificates() {
    try {
        certificates = await API.getCertificates();
        displayCertificates();
    } catch (error) {
        showError('certificates-list', error.message);
    }
}

function displayCertificates() {
    const container = document.getElementById('certificates-list');
    if (!certificates || certificates.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-award"></i><p>Nu există certificate</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Număr</th>
                        <th>Voluntar</th>
                        <th>Ore Totale</th>
                        <th>Data Emiterii</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${certificates.map(cert => `
                        <tr>
                            <td>${cert.id}</td>
                            <td>${cert.certificateNumber}</td>
                            <td>${cert.volunteer?.firstName || ''} ${cert.volunteer?.lastName || ''}</td>
                            <td>${cert.totalHours || 0}</td>
                            <td>${cert.issueDate || '-'}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editCertificate(${cert.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteCertificate(${cert.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showCertificateForm(id = null) {
    const cert = id ? certificates.find(c => c.id === id) : null;
    const modal = createModal('certificate-modal', 'Certificat', `
        <form id="certificate-form">
            <input type="hidden" id="cert-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Voluntar ID *</label>
                <input type="number" class="form-control" id="cert-volunteer-id" value="${cert?.volunteer?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Număr Certificat *</label>
                <input type="text" class="form-control" id="cert-number" value="${cert?.certificateNumber || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Ore Totale *</label>
                <input type="number" step="0.5" class="form-control" id="cert-hours" value="${cert?.totalHours || 0}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Data Emiterii</label>
                <input type="date" class="form-control" id="cert-issue-date" value="${cert?.issueDate || ''}">
            </div>
        </form>
    `, async () => {
        const formData = {
            volunteer: { id: parseInt(document.getElementById('cert-volunteer-id').value) },
            certificateNumber: document.getElementById('cert-number').value,
            totalHours: parseFloat(document.getElementById('cert-hours').value),
            issueDate: document.getElementById('cert-issue-date').value || new Date().toISOString().split('T')[0]
        };
        
        try {
            if (id) {
                await API.updateCertificate(id, formData);
            } else {
                await API.createCertificate(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('certificate-modal')).hide();
            loadCertificates();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('certificate-modal')).show();
}

async function editCertificate(id) {
    showCertificateForm(id);
}

async function deleteCertificate(id) {
    if (!confirm('Ești sigur că vrei să ștergi acest certificat?')) return;
    try {
        await API.deleteCertificate(id);
        loadCertificates();
        showSuccess('Certificat șters!');
    } catch (error) {
        showError('certificates-list', error.message);
    }
}

// ========== FEEDBACK ==========
async function loadFeedback() {
    try {
        feedback = await API.getFeedback();
        displayFeedback();
    } catch (error) {
        showError('feedback-list', error.message);
    }
}

function displayFeedback() {
    const container = document.getElementById('feedback-list');
    if (!feedback || feedback.length === 0) {
        container.innerHTML = '<div class="empty-state"><i class="bi bi-chat-left-text"></i><p>Nu există feedback</p></div>';
        return;
    }
    
    container.innerHTML = `
        <div class="table-responsive">
            <table class="table table-hover">
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Asignare ID</th>
                        <th>Rating</th>
                        <th>Tip</th>
                        <th>Data</th>
                        <th>Acțiuni</th>
                    </tr>
                </thead>
                <tbody>
                    ${feedback.map(fb => `
                        <tr>
                            <td>${fb.id}</td>
                            <td>${fb.assignment?.id || '-'}</td>
                            <td>${'⭐'.repeat(fb.rating || 0)}</td>
                            <td><span class="badge bg-info">${fb.feedbackType}</span></td>
                            <td>${fb.feedbackDate || '-'}</td>
                            <td>
                                <button class="btn btn-sm btn-warning" onclick="editFeedback(${fb.id})">
                                    <i class="bi bi-pencil"></i>
                                </button>
                                <button class="btn btn-sm btn-danger" onclick="deleteFeedback(${fb.id})">
                                    <i class="bi bi-trash"></i>
                                </button>
                            </td>
                        </tr>
                    `).join('')}
                </tbody>
            </table>
        </div>
    `;
}

function showFeedbackForm(id = null) {
    const fb = id ? feedback.find(f => f.id === id) : null;
    const modal = createModal('feedback-modal', 'Feedback', `
        <form id="feedback-form">
            <input type="hidden" id="fb-id" value="${id || ''}">
            <div class="mb-3">
                <label class="form-label">Asignare ID *</label>
                <input type="number" class="form-control" id="fb-assignment-id" value="${fb?.assignment?.id || ''}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Rating (1-5) *</label>
                <input type="number" min="1" max="5" class="form-control" id="fb-rating" value="${fb?.rating || 5}" required>
            </div>
            <div class="mb-3">
                <label class="form-label">Comentariu</label>
                <textarea class="form-control" id="fb-comment">${fb?.comment || ''}</textarea>
            </div>
            <div class="mb-3">
                <label class="form-label">Tip</label>
                <select class="form-select" id="fb-type">
                    <option value="ORG_TO_VOLUNTEER" ${fb?.feedbackType === 'ORG_TO_VOLUNTEER' ? 'selected' : ''}>Organizație → Voluntar</option>
                    <option value="VOLUNTEER_TO_ORG" ${fb?.feedbackType === 'VOLUNTEER_TO_ORG' ? 'selected' : ''}>Voluntar → Organizație</option>
                </select>
            </div>
        </form>
    `, async () => {
        const formData = {
            assignment: { id: parseInt(document.getElementById('fb-assignment-id').value) },
            rating: parseInt(document.getElementById('fb-rating').value),
            comment: document.getElementById('fb-comment').value,
            feedbackType: document.getElementById('fb-type').value
        };
        
        try {
            if (id) {
                await API.updateFeedback(id, formData);
            } else {
                await API.createFeedback(formData);
            }
            bootstrap.Modal.getInstance(document.getElementById('feedback-modal')).hide();
            loadFeedback();
            showSuccess('Operațiune reușită!');
        } catch (error) {
            showError('modal-error', error.message);
        }
    });
    
    new bootstrap.Modal(document.getElementById('feedback-modal')).show();
}

async function editFeedback(id) {
    showFeedbackForm(id);
}

async function deleteFeedback(id) {
    if (!confirm('Ești sigur că vrei să ștergi acest feedback?')) return;
    try {
        await API.deleteFeedback(id);
        loadFeedback();
        showSuccess('Feedback șters!');
    } catch (error) {
        showError('feedback-list', error.message);
    }
}

// ========== STATISTICS ==========
async function loadStatistics(type) {
    const container = document.getElementById('statistics-content');
    container.innerHTML = '<div class="loading"><i class="bi bi-arrow-repeat spin"></i> Se încarcă...</div>';
    
    try {
        let data;
        let params = {};
        
        if (type === 'hours-per-volunteer') {
            params.limit = 10;
        }
        
        data = await API.getStatistics(type, params);
        displayStatistics(type, data);
    } catch (error) {
        container.innerHTML = `<div class="alert alert-danger">${error.message}</div>`;
    }
}

function displayStatistics(type, data) {
    const container = document.getElementById('statistics-content');
    
    if (type === 'overview') {
        container.innerHTML = `
            <div class="row">
                <div class="col-md-3">
                    <div class="stats-card">
                        <h5>${data.totalOrganizations || 0}</h5>
                        <p>Organizații</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h5>${data.totalVolunteers || 0}</h5>
                        <p>Voluntari</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h5>${data.totalProjects || 0}</h5>
                        <p>Proiecte</p>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="stats-card">
                        <h5>${data.totalEvents || 0}</h5>
                        <p>Evenimente</p>
                    </div>
                </div>
            </div>
        `;
    } else if (Array.isArray(data)) {
        container.innerHTML = `
            <div class="stats-table">
                <table class="table">
                    <thead>
                        <tr>
                            ${Object.keys(data[0] || {}).map(key => `<th>${key}</th>`).join('')}
                        </tr>
                    </thead>
                    <tbody>
                        ${data.map(row => `
                            <tr>
                                ${Object.values(row).map(val => `<td>${val}</td>`).join('')}
                            </tr>
                        `).join('')}
                    </tbody>
                </table>
            </div>
        `;
    } else {
        container.innerHTML = `<pre>${JSON.stringify(data, null, 2)}</pre>`;
    }
}

// ========== UTILITY FUNCTIONS ==========
function createModal(id, title, body, onSave) {
    const modalHtml = `
        <div class="modal fade" id="${id}" tabindex="-1">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">${title}</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div id="modal-error"></div>
                        ${body}
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Anulează</button>
                        <button type="button" class="btn btn-primary" id="modal-save-btn-${id}">Salvează</button>
                    </div>
                </div>
            </div>
        </div>
    `;
    
    // Insert modal and attach handler
    document.getElementById('modals-container').innerHTML = modalHtml;
    const modalElement = document.getElementById(id);
    const saveBtn = document.getElementById(`modal-save-btn-${id}`);
    
    if (saveBtn && onSave) {
        saveBtn.addEventListener('click', onSave);
    }
    
    return modalElement;
}

function showError(containerId, message) {
    const container = document.getElementById(containerId);
    container.innerHTML = `<div class="alert alert-danger">${message}</div>`;
    setTimeout(() => {
        if (container.innerHTML.includes('alert-danger')) {
            container.innerHTML = '';
        }
    }, 5000);
}

function showSuccess(message) {
    const alert = document.createElement('div');
    alert.className = 'alert alert-success alert-dismissible fade show position-fixed top-0 end-0 m-3';
    alert.style.zIndex = '9999';
    alert.innerHTML = `
        ${message}
        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    `;
    document.body.appendChild(alert);
    setTimeout(() => alert.remove(), 3000);
}

function getStatusColor(status) {
    const colors = {
        'ACTIVE': 'success',
        'INACTIVE': 'secondary',
        'PENDING': 'warning',
        'COMPLETED': 'info',
        'CANCELLED': 'danger',
        'DRAFT': 'secondary',
        'SCHEDULED': 'primary',
        'ONGOING': 'info',
        'ACCEPTED': 'success',
        'REJECTED': 'danger'
    };
    return colors[status] || 'secondary';
}

function formatDateTime(dateTime) {
    if (!dateTime) return '-';
    const date = new Date(dateTime);
    return date.toLocaleString('ro-RO');
}

function formatDateTimeForInput(dateTime) {
    if (!dateTime) return '';
    const date = new Date(dateTime);
    return date.toISOString().slice(0, 16);
}
