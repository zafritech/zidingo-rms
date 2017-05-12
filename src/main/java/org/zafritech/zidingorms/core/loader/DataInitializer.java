/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.loader;

import java.util.HashSet;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.core.commons.FolderTypes;
import org.zafritech.zidingorms.core.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.ArtifactType;
import org.zafritech.zidingorms.database.domain.Company;
import org.zafritech.zidingorms.database.domain.Folder;
import org.zafritech.zidingorms.database.domain.ItemType;
import org.zafritech.zidingorms.database.domain.LinkType;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.Role;
import org.zafritech.zidingorms.database.domain.SystemVariable;
import org.zafritech.zidingorms.database.domain.User;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ArtifactTypeRepository;
import org.zafritech.zidingorms.database.repositories.CompanyRepository;
import org.zafritech.zidingorms.database.repositories.FolderRepository;
import org.zafritech.zidingorms.database.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.database.repositories.LinkTypeRepository;
import org.zafritech.zidingorms.database.repositories.RoleRepository;
import org.zafritech.zidingorms.database.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.database.repositories.UserRepository;
import org.zafritech.zidingorms.projects.ProjectServiceImpl;

/**
 *
 * @author LukeS
 */
@Component
public class DataInitializer {

    @Autowired
    private RoleRepository roleRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private CompanyRepository companyRepository;
    
    @Autowired
    private ProjectServiceImpl projectService;
    
    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private ArtifactTypeRepository artifactTypeRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;
   
    @Autowired
    private LinkTypeRepository linkTypeRepository;
    
    @Autowired
    private SystemVariableRepository systemVariableRepository;
    
    public DataInitializer() {
        
    }
    
    @Transactional
    public void initializeRoles() {
        
        roleRepository.save(new Role("ROLE_ADMIN", "Administrator"));
        roleRepository.save(new Role("ROLE_MANAGER", "Manager"));
        roleRepository.save(new Role("ROLE_USER", "Application User"));
        roleRepository.save(new Role("ROLE_ACTUATOR", "System Actuator"));
        roleRepository.save(new Role("ROLE_GUEST", "Guest User"));
        
        System.out.println("Zidingo RMS: Roles initialized....");
    }
    
    @Transactional
    public void initializeUsers() {

        // Create users and assign roles
        userRepository.save(new HashSet<User>() {
            {

                add(new User("admin@zafritech.org", "admin", new HashSet<Role>() {
                    {
                        add(roleRepository.findByRoleName("ROLE_ADMIN"));
                        add(roleRepository.findByRoleName("ROLE_MANAGER"));
                    }
                }));

                add(new User("luke.sibisi@astad.qa", "Password@123", new HashSet<Role>() {
                    {
                        add(roleRepository.findByRoleName("ROLE_ADMIN"));
                        add(roleRepository.findByRoleName("ROLE_MANAGER"));
                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("client@astad.qa", "password", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_GUEST"));
                    }
                }));

                add(new User("contractor@astad.qa", "password", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_GUEST"));
                    }
                }));

                add(new User("abdelazim.shehab@astad.qa", "Password@123", new HashSet<Role>() {{
                    
                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("sheraz.butt@astad.qa", "Password@123", new HashSet<Role>() {{
                    
                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("fayez.shahin@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("balakrishnan.nair@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("shahid.altaf@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("shafik.salamor@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("nuno.barbosa@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("nagy.fahmy@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("rogelio.mercado@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("sanjay.thakur@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("ahmet.hayta@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("anthony.mopty@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("mohamed.eldarandaly@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("manivel.subramaniyan@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("mohamed.megahed@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("nedyalka.ivanova@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("augustine.ogbolu@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("edirisinghe.tissa@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("ragip.sevim@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("ibrahim.korayem@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("ellen.santos@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("bilal.daas@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));

                add(new User("lillani.herath@astad.qa", "Password@123", new HashSet<Role>() {{

                        add(roleRepository.findByRoleName("ROLE_USER"));
                    }
                }));
            }
        });
        
        System.out.println("Zidingo RMS: Users initialized....");
    }

    @Transactional
    public void initializeProjects() {

        companyRepository.save(new Company("Qatar Foundation WLL", "Qatar Foundation"));
        Company company1 = companyRepository.findByCompanyName("Qatar Foundation WLL");

        companyRepository.save(new Company("Zafritech System (Pty) Ltd.", "Zafritech"));
        Company company2 = companyRepository.findByCompanyName("Zafritech System (Pty) Ltd.");
        
        // projectFromTemplate("Rail-Metro", "Zidingo RMS");
        Project project1 = projectService.create("Education City People Mover System", "Qatar Foundation PMS", company2);
        Project project2 = projectService.create("Zidingo Requirements Management System", "Zidingo RMS", company1);
        
        // Empty projects #########################
        // Seed Project #1
        Folder folder1 = folderRepository.save(new Folder(project1.getProjectShortName(), FolderTypes.PROJECT, project1));
        Folder input1 = folderRepository.save(new Folder("Input Documents", FolderTypes.DOCUMENT, folder1, project1));
        Folder output1 = folderRepository.save(new Folder("Verification & Validation", FolderTypes.DOCUMENT, folder1, project1));
        
        Folder contract1 = folderRepository.save(new Folder("Contract", FolderTypes.DOCUMENT, input1, project1));
        Folder thirdpty1 = folderRepository.save(new Folder("Third Party Sources", FolderTypes.DOCUMENT, input1, project1));
        
        Folder gencon1 = folderRepository.save(new Folder("General Conditions", FolderTypes.DOCUMENT, contract1, project1));
        Folder appenx1 = folderRepository.save(new Folder("Appendices", FolderTypes.DOCUMENT, contract1, project1));
        
        artifactRepository.save(new Artifact("XC08100100-ART-01", "Article 01", "Article 01 - DEFINITIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-02", "Article 02", "Article 02 - CONTRACT INTERPRETATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));

        artifactRepository.save(new Artifact("XC08100100-APP-A", "Appendix A", "Appendix A - Scope of Work and Specifications", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        
        artifactRepository.save(new Artifact("XC08100100-VVR-A", "V & V References", "Appendix B - Verification & Validation References", artifactTypeRepository.findByArtifactTypeName("VRS"), project1, output1));
        artifactRepository.save(new Artifact("XC08100100-VVE-A", "V & V References", "Appendix B - Verification & Validation Evidence", artifactTypeRepository.findByArtifactTypeName("VRS"), project1, output1));
 
        // Seed Project #2 #########################
        Folder folder2 = folderRepository.save(new Folder(project2.getProjectShortName(), FolderTypes.PROJECT, project2));
        Folder input2 = folderRepository.save(new Folder("Input Documents", FolderTypes.DOCUMENT, folder2, project2));
        Folder specn2 = folderRepository.save(new Folder("Specifications", FolderTypes.DOCUMENT, folder2, project2));
        Folder desig2 = folderRepository.save(new Folder("Design", FolderTypes.DOCUMENT, folder2, project2));

        artifactRepository.save(new Artifact("ZID-RLS-1700", "Requirements List", "Zidingo RMS Requirements List", artifactTypeRepository.findByArtifactTypeName("RLS"), project2, input2));
        artifactRepository.save(new Artifact("ZID-SRS-1701", "Zidingo SRS", "Zidingo RMS Software Requirements Specification", artifactTypeRepository.findByArtifactTypeName("SRS"), project2, specn2));
        artifactRepository.save(new Artifact("ZID-SDD-1702", "Zidingo SDD", "Zidingo RMS Software Design Description", artifactTypeRepository.findByArtifactTypeName("SDD"), project2, desig2));
        
        System.out.println("Zidingo RMS: Projects initialized....");
    }
    
    @Transactional
    public void initializeArtifactTypes() {

        artifactTypeRepository.save(new HashSet<ArtifactType>() {{

                add(new ArtifactType("GEN", "Generic Project Documennt", "This document type is for a generic project document that does not fall under specifications. It may or may not contain requirements. Plans, reports, procudures etc. fall under this category."));
                add(new ArtifactType("RLS", "Requirements List", "This document is a preliminary collection of system requirements that are discovered from interection with stakeholders. The requirements act as an input to the formal SyRS."));
                add(new ArtifactType("URS", "User Requirements Specification", "A User Requirements Specification (a.k.a. Stakeholder Requirements Specification) specifies the requirements the user expects from the system to be constructed. User Requirements are the top level of requirements. They capture the needs of users, the customer and other sources of requirements like legal regulations and internal company high level requirements."));
                add(new ArtifactType("SyRS", "System Requirements Specification", "A System Requirements Specification contains the next level of requirements after user requirements. The aim of system requirements is to set precise technical requirements for the system development. System requirements are derived from user requirements by considering existing technology, components and so on."));
                add(new ArtifactType("VRS", "Verification Requirements Specification", "The Verification Requirements Specification (VRS) describes the qualities of the evidence required that a set of requirements defining an item is satisfied. The item may be of any nature whatsoever, ranging from, for example, a physical object, to software, to an interface, to a data item, to a material, or to a service."));
                add(new ArtifactType("SyDD", "System Design Description", "The System/Subsystem Design Description (aka SSDD) describes the system- or subsystem-wide design and the architectural design of a system or subsystem. The SyDD may be supplemented by Interface Design Descriptions  and Database Design Descriptions."));
                add(new ArtifactType("IRS", "Interface Requirements Specification", "Interface Requirements Specification (IRS) specifies the requirements imposed on one or more systems, subsystems, Hardware Configuration Items (HWCIs), Computer Software Configuration Items (CSCIs), manual operations, or other system components to achieve one or more interfaces among these entities."));
                add(new ArtifactType("ICD", "Interface Control Document", "Interface Control Document (ICD) is a document that describes the interface(s) to a system or subsystem. It may describe the inputs and outputs of a single system or the interface between two systems or subsystems. It can be very detailed or pretty high level, but the point is to describe all inputs to and outputs from a system"));
                add(new ArtifactType("SRS", "Software Requirements Specification", "A software requirements specification (SRS) is a description of a software system to be developed. It lays out functional and non-functional requirements, and may include a set of use cases that describe user interactions that the software must provide."));
                add(new ArtifactType("SDD", "Software Design Description", "A software design description (aka software design document or SDD) is a written description of a software product, that a software designer writes in order to give a software development team overall guidance to the architecture of the software project."));

            }
        });
        
        System.out.println("Zidingo RMS: ArtifactTypes initialized....");
    }
    
    @Transactional
    public void initializeItemTypes() {

        itemTypeRepository.save(new HashSet<ItemType>() {
            {

                add(new ItemType("State/Mode", "STMD", "States/Modes Requirement", ""));
                add(new ItemType("Functional", "FCNL", "Functional Requirement", "A functional requirement describes a function of the system, i.e. what the system must do resp. a service the system provides to its users."));
                add(new ItemType("Performance", "PERF", "Performance Requirement", "A performance requirement is a non-functional requirement that describes the amount of useful work accomplished by the system compared to the time and resources used."));
                add(new ItemType("Constraint", "CONS", "Constraint Requirement", "Constraint Requirement describes real-world limits or boundaries around what we want to happen"));
                add(new ItemType("Design", "DSGN", "Design Requirement", "Design requirements direct the design (internals of the system), by inclusion (build it this way), or exclusion (don't build it this way)."));
                add(new ItemType("Scalability", "SCAL", "Scalability Requirement", "A scalability requirement is a non-functional requirement that describes the ability of the system to handle growing amounts of work in a graceful manner or its ability to be enlarged to accommodate that growth."));
                add(new ItemType("Security", "SCRT", "Security Requirement", "A security requirement is a non-functional requirement that describes the stipulated degree of the systems protection against danger, damage, misuse, unauthorized access, data loss and crime."));
                add(new ItemType("Maintainability", "MANT", "Maintainability Requirement", "A maintainability requirement is a non-functional requirement that describes the ease with which the system can be maintained in order to isolate defects or their cause, correct defects or their cause, meet new requirements, make future maintenance easier, or cope with a changed environment."));
                add(new ItemType("Interface", "INRF", "External Interface Requirement", ""));
                add(new ItemType("Environment", "ENVR", "Environment Requirement", ""));
                add(new ItemType("Resource", "RESC", "Resource Requirement", ""));
                add(new ItemType("Physical", "PHYL", "Physical Requirement", ""));
                add(new ItemType("Usability", "USBL", "Usability Requirement", "A usability requirement is a non-functional requirement describing the intended ease of use (ergonomical comfort) and learnability of the system."));
                add(new ItemType("Legal", "LEGL", "Legal Requirement", "A legal requirement is a non-functional requirement that states a regulation that must be recognized by the system. Regulations could be laws, standards, specifications, etc."));
                add(new ItemType("Story", "STRY", "User Story", "A (user) story is a special kind of functional requirement, which uses one or more sentences in the everyday or business language of the end user that captures what the user (resp. a role) wants to achieve. User stories generally follow the following template: \"As a <role>, I want <goal/desire> so that <benefit>.\""));
                add(new ItemType("Unclassified", "UNCL", "Unclassified Requirement", "Requirements not classified."));
                add(new ItemType("None", "NONE", "Not a Requirement", "Prose textual information for clarification and context setting for a set of requirements."));
                add(new ItemType("Other Quality", "OTHR", "Other Quality Requirement", ""));
                add(new ItemType("Operational", "OPER", "Operational Requirement", ""));
                add(new ItemType("Adaptability", "ADPT", "Adaptability Requirement", ""));
                add(new ItemType("Logistical", "LOGS", "Logistical Requirement", ""));
                add(new ItemType("Policy", "POLY", "Policy and Regulations", ""));
                add(new ItemType("Cost/Schedule", "COST", "Cost and Schedule Constraint", ""));
            }
        });
        
        System.out.println("Zidingo RMS: ItemTypes initialized....");
    }
    
    @Transactional
    public void initializeLinkTypes() {
        
        linkTypeRepository.save(new HashSet<LinkType>() {
            
            {
                add(new LinkType("Derived", "Derived From", "Captures the relationship between a requirement artifact and an architecture management item that represents a model of the requirement artifact. For example, a UML use case in an architecture management application can represent a requirement artifact. In architecture management applications, links of this type are shown as Derives From Architecture Element links.", true));
                add(new LinkType("Affected", "Affected By", "Captures the relationship between a requirement artifact and a change management item that affects the implementation of the requirement artifact. For example, a defect in the Change and Configuration Management (CCM) application can affect the implementation of a requirement artifact. In the CCM application, links of this type are shown as Affects links.", false));
                add(new LinkType("Implements", "Implements", "Captures the relationship between a requirement artifact and a change management item that describes the implementation of the requirement artifact. For example, a story in the CCM application can describe the implementation of a requirement artifact. In the CCM application, links of this type are shown as Implements links.", false));
                add(new LinkType("Satisfies", "Satisfies or Is satisfied By", "Captures how the different levels of requirements are elaborated. For example: an approved vision statement in a vision document can be satisfied by one or more stakeholder requirements.", false));
                add(new LinkType("Validates", "Validates", "Captures the relationship between a requirement artifact and a test artifact that validates the implementation of the requirement artifact. For example, a test plan in the Quality Management (QM) application can validate the implementation of a requirement artifact. In the QM application, links of this type are displayed as Validates links.", false));
                add(new LinkType("Referenced", "Referenced By or References", "Tracks a relationship between requirement artifacts. These types of relationships occur when creating links between artifacts.", false));
                add(new LinkType("Refines", "Refines", "", false));
                add(new LinkType("Tracks", "Tracks or Is Tracked By", "Captures the relationship between a requirement artifact and a change management item that tracks the implementation of the requirement artifact. For example, a task in the CCM application can track the implementation of a requirement artifact. In the CCM application, links of this type are shown as Tracks links.", false));
                add(new LinkType("Refers", "Refers To", "", false));
                add(new LinkType("Embedded", "Embedded In", "Tracks a containment relationship between RM artifacts. These types of relationships occur when you complete operations such as inserting an artifact and inserting an image for a text artifact.", false));
                add(new LinkType("Extracted", "Extracted From", "Captures when the content of a requirement artifact was created from the contents of another requirement artifact. This type of link is created during extraction-based operations; for example, when you create an artifact by saving an existing artifact as a new artifact.", false));
                add(new LinkType("Illustrates", "Illustrates", "Illustrates the relationships between graphical and text artifacts.", false));
                add(new LinkType("Links", "Links To", "Tracks a general relationship between requirement artifacts.", false));
                add(new LinkType("Mitigates", "Mitigates", "Captures the relationship between requirements and risks. A requirement mitigates one or more risks, and a risk is mitigated by one or more requirements.", false));
                add(new LinkType("Decomposition", "Is a Decomposition Of", "Captures part-whole relationships between requirement artifacts. Typically, these types of links represent artifact hierarchies.", false));
                add(new LinkType("Constrained", "Is Constrained By", "Captures the relationship between requirement artifacts when one artifact limits or holds back the other artifact. For example, an artifact can be constrained by a requirement that it must conform to", false));
            }
        });
        
        System.out.println("Zidingo RMS: LinkTypes initialized....");
    }
    
    @Transactional
    public void initializeSystemVariable() {

        Iterable<Artifact> artifacts = artifactRepository.findAll();

        for (Artifact artifact : artifacts) {

            String uuidTemplate = artifact.getArtifactName().substring(0, 3).toUpperCase() + "-ID" + String.format("%02d", artifact.getId());
            String reqIdTemplate = artifact.getArtifactName().substring(0, 2).toUpperCase() + String.format("%02d", artifact.getId());

            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", artifact.getId()));

            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_TEMPLATE.name(), uuidTemplate, "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-GENL", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-INTF", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-FCNL", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-STMD", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-ENVR", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-PERF", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-RSRC", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-PHCL", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-DSGN", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-CNST", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-QLTY", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-SAFT", "DOCUMENT", artifact.getId()));
            systemVariableRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-VAVN", "DOCUMENT", artifact.getId()));

        }
        
        System.out.println("Zidingo RMS: SystemVariables initialized....");
    }
}
