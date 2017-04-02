/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms;

import java.util.HashSet;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.commons.FolderTypes;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.ArtifactType;
import org.zafritech.zidingorms.domain.Company;
import org.zafritech.zidingorms.domain.Folder;
import org.zafritech.zidingorms.domain.ItemType;
import org.zafritech.zidingorms.domain.Project;
import org.zafritech.zidingorms.domain.Role;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ArtifactTypeRepository;
import org.zafritech.zidingorms.repositories.CompanyRepository;
import org.zafritech.zidingorms.repositories.FolderRepository;
import org.zafritech.zidingorms.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.repositories.ProjectRepository;
import org.zafritech.zidingorms.repositories.RoleRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.repositories.UserRepository;
import org.zafritech.zidingorms.services.impl.ProjectServiceImpl;

/**
 *
 * @author LukeS
 */
@Component
public class StarterCLR implements CommandLineRunner {

    @Autowired
    private SystemVariableRepository sysVarRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ArtifactTypeRepository artifactTypeRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Autowired
    private ProjectServiceImpl projectService;

    @Override
    public void run(String... strings) throws Exception {

//    	SeedRoles();
//        SeedUsers();
//        SeedItemTypes();
//        SeedArtifactTypes();
//        SeedProjects();
//    	SeedSystemVariable();
    }

    @Transactional
    private void SeedRoles() {

        roleRepository.save(new Role("ROLE_ADMIN"));
        roleRepository.save(new Role("ROLE_MANAGER"));
        roleRepository.save(new Role("ROLE_USER"));
        roleRepository.save(new Role("ROLE_ACTUATOR"));
        roleRepository.save(new Role("ROLE_GUEST"));

    }

    @Transactional
    private void SeedUsers() {

        // Create users and assign roles
        userRepository.save(new HashSet<User>() {
            {

                add(new User("admin@zafritech.org", "admin", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_ADMIN"));
                        add(roleRepository.findByRoleName("ROLE_MANAGER"));

                    }
                }));

                add(new User("luke@zafritech.org", "password", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_USER"));
                        add(roleRepository.findByRoleName("ROLE_MANAGER"));

                    }
                }));

                add(new User("mbali@zafritech.org", "password", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_USER"));

                    }
                }));

                add(new User("ndumiso@zafritech.org", "password", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_USER"));

                    }
                }));

                add(new User("khosi@zafritech.org", "password", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_USER"));

                    }
                }));

                add(new User("guest@zafritech.org", "password", new HashSet<Role>() {
                    {

                        add(roleRepository.findByRoleName("ROLE_GUEST"));

                    }
                }));
            }
        });

        roleRepository.findAll().forEach(System.out::println);
        userRepository.findAll().forEach(System.out::println);
    }

    @Transactional
    private void SeedProjects() {

        companyRepository.save(new Company("Qatar Foundation WLL", "Qatar Foundation"));
        Company company1 = companyRepository.findByCompanyName("Qatar Foundation WLL");

        companyRepository.save(new Company("Zafritech System (Pty) Ltd.", "Zafritech"));
        Company company2 = companyRepository.findByCompanyName("Zafritech System (Pty) Ltd.");

        // projectFromTemplate("rail-metro", "Zidingo RMS");
        Project project1 = projectService.create("Education City People Mover System", "Qatar Foundation PMS", company2);
        Project project2 = projectService.create("Zidingo Requirements Management System", "Zidingo RMS", company1);

        // Empty projects #########################
//        projectService.create("Project Drummer II", "Project Drummer II", company1);
//        projectService.create("Project Rooivalk AH III", "Project Rooivalk AH III", company2);
//        projectService.create("Project Phoenix", "Project Phoenix", company1);
//        projectService.create("Lukuluba Upgrade 2020", "Lukuluba Upgrade 2020", company2);
        // Seed Project #1
        Folder folder1 = folderRepository.save(new Folder(project1.getProjectShortName(), FolderTypes.PROJECT, project1));
        Folder input1 = folderRepository.save(new Folder("Input Documents", FolderTypes.DOCUMENT, folder1, project1));
//        Folder specn1 = folderRepository.save(new Folder("Specifications", FolderTypes.DOCUMENT, folder1, project1));
//        Folder desig1 = folderRepository.save(new Folder("Architectural Design", FolderTypes.DOCUMENT, folder1, project1));
//        Folder detal1 = folderRepository.save(new Folder("Detailed Specification", FolderTypes.DOCUMENT, folder1, project1));

        Folder contract1 = folderRepository.save(new Folder("Contract", FolderTypes.DOCUMENT, input1, project1));
//        Folder internal1 = folderRepository.save(new Folder("Internal Sources", FolderTypes.DOCUMENT, input1, project1));
        Folder thirdpty1 = folderRepository.save(new Folder("Third Party Sources", FolderTypes.DOCUMENT, input1, project1));

//        folderRepository.save(new Folder("Electrical System", FolderTypes.DOCUMENT, detal1, project1));
//        folderRepository.save(new Folder("Communication System", FolderTypes.DOCUMENT, detal1, project1));
//        folderRepository.save(new Folder("Subsystem #1", FolderTypes.DOCUMENT, detal1, project1));
        Folder gencon1 = folderRepository.save(new Folder("General Conditions", FolderTypes.DOCUMENT, contract1, project1));
        Folder appenx1 = folderRepository.save(new Folder("Appendices", FolderTypes.DOCUMENT, contract1, project1));

        folderRepository.save(new Folder("EN Standards", FolderTypes.DOCUMENT, thirdpty1, project1));
        folderRepository.save(new Folder("Railway Safety Board", FolderTypes.DOCUMENT, thirdpty1, project1));
        folderRepository.save(new Folder("Electricity Authority", FolderTypes.DOCUMENT, thirdpty1, project1));
        folderRepository.save(new Folder("Depart. of Transport", FolderTypes.DOCUMENT, thirdpty1, project1));

        artifactRepository.save(new Artifact("XC08100100-ART-01", "Article 01", "Article 01 - DEFINITIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-02", "Article 02", "Article 02 - CONTRACT INTERPRETATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-03", "Article 03", "Article 03 - SCOPE OF WORK", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-04", "Article 04", "Article 04 - CONTRACTOR PERFORMANCE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-05", "Article 05", "Article 05 - CONTRACTOR PERSONNEL", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-06", "Article 06", "Article 06 - MATERIALS, EQUIPMENT AND FACILITIES PROVIDED BY CONTRACTOR", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-07", "Article 07", "Article 07 - MATERIALS, EQUIPMENT AND FACILITIES PROVIDED BY QF", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-08", "Article 08", "Article 08 - INSPECTION, TESTING AND APPROVAL OF THE WORK", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-09", "Article 09", "Article 09 - EXECUTION PROGRAMME, COMPLETION AND ACCEPTANCE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-10", "Article 10", "Article 10 - LIQUIDATED DAMAGES FOR DELAYED COMPLETION", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-11", "Article 11", "Article 11 - CONTRACT PRICE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-12", "Article 12", "Article 12 - TERMS OF PAYMENT", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-13", "Article 13", "Article 13 - GUARANTEE OF WORK AND BANK GUARANTEE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-14", "Article 14", "Article 14 - VARIATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-15", "Article 15", "Article 15 - REPRESENTATIVES", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-16", "Article 16", "Article 16 - ASSIGNMENT AND SUB-CONTRACTING", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-17", "Article 17", "Article 17 - FORCE MAJEURE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-18", "Article 18", "Article 18 - SUSPENSION", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-19", "Article 19", "Article 19 - TERMINATION", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-20", "Article 20", "Article 20 - LIABILITIES AND INDEMNITIES", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-21", "Article 21", "Article 21 - INSURANCE", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-22", "Article 22", "Article 22 - TAXES AND GOVERNMENT CHARGES", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-23", "Article 23", "Article 23 - CONFIDENTIALITY AND SECRECY", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-24", "Article 24", "Article 24 - INVENTIONS AND LICENSES", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-25", "Article 25", "Article 25 - TITLE AND LIENS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-26", "Article 26", "Article 26 - ACCOUNTING RECORDS AND AUDIT RIGHTS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-27", "Article 27", "Article 27 - SEVERABILITY", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-28", "Article 28", "Article 28 - LAWS AND REGULATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-29", "Article 29", "Article 29 - SETTLEMENT OF DISPUTES", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-30", "Article 30", "Article 30 - SURVIVAL OF PROVISIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-31", "Article 31", "Article 31 - PUBLIC RELATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-32", "Article 32", "Article 32 - NOTICES AND COMMUNICATIONS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-33", "Article 33", "Article 33 - CONFLICT OF INTEREST AND BUSINESS ETHICS", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));
        artifactRepository.save(new Artifact("XC08100100-ART-34", "Article 24", "Article 34 - QF AGENT", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, gencon1));

        artifactRepository.save(new Artifact("XC08100100-APP-A", "Appendix A", "Appendix A - Scope of Work and Specifications", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-B", "Appendix B", "Appendix B - Schedule of Prices", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-C", "Appendix C", "Appendix C - Insurance", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-D", "Appendix D", "Appendix D - Administration Instructions", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-E", "Appendix E", "Appendix E - Contractor Resources", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-F", "Appendix F", "Appendix F - Drawings", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-F", "Appendix G", "Appendix G - Materials, Equipment and Facilities provided by QF", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));
        artifactRepository.save(new Artifact("XC08100100-APP-F", "Appendix H", "Appendix H - Contract Execution Plan", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, appenx1));

//        artifactRepository.save(new Artifact("Z822-SYS-RQL-1201", "Requirements Lists", artifactTypeRepository.findByArtifactTypeName("RLS"), project1, internal1));
//        artifactRepository.save(new Artifact("ZFTC-GEN-STD-0002", "Design Standard", artifactTypeRepository.findByArtifactTypeName("GEN"), project1, internal1));
//        artifactRepository.save(new Artifact("Z822-SYS-SSS-2201", "System Specification", artifactTypeRepository.findByArtifactTypeName("SyRS"), project1, specn1));
//        artifactRepository.save(new Artifact("Z822-SYS-VRS-2202", "Validation Test Spec", artifactTypeRepository.findByArtifactTypeName("VRS"), project1, specn1));
//
//        artifactRepository.save(new Artifact("Z822-SYS-SDD-2301", "Architectural Description", artifactTypeRepository.findByArtifactTypeName("SyDD"), project1, desig1));
//        artifactRepository.save(new Artifact("Z822-SYS-SDD-2302", "Design Description", artifactTypeRepository.findByArtifactTypeName("SyDD"), project1, desig1));
//        artifactRepository.save(new Artifact("Z822-SYS-ICD-2303", "System Main ICD", artifactTypeRepository.findByArtifactTypeName("ICD"), project1, desig1));
        // Seed Project #2 #########################
        Folder folder2 = folderRepository.save(new Folder(project2.getProjectShortName(), FolderTypes.PROJECT, project2));
        Folder input2 = folderRepository.save(new Folder("Input Documents", FolderTypes.DOCUMENT, folder2, project2));
        Folder specn2 = folderRepository.save(new Folder("Specifications", FolderTypes.DOCUMENT, folder2, project2));
        Folder desig2 = folderRepository.save(new Folder("Architectural Design", FolderTypes.DOCUMENT, folder2, project2));
        Folder detal2 = folderRepository.save(new Folder("Detailed Specification", FolderTypes.DOCUMENT, folder2, project2));

        Folder contract2 = folderRepository.save(new Folder("Contract", FolderTypes.DOCUMENT, input2, project2));
        Folder internal2 = folderRepository.save(new Folder("Internal Sources", FolderTypes.DOCUMENT, input2, project2));
        Folder thirdpty2 = folderRepository.save(new Folder("Third Party Sources", FolderTypes.DOCUMENT, input2, project2));

        folderRepository.save(new Folder("System Specification", FolderTypes.DOCUMENT, specn2, project2));
        folderRepository.save(new Folder("Validation Test Spec", FolderTypes.DOCUMENT, specn2, project2));

        folderRepository.save(new Folder("Architectural Description", FolderTypes.DOCUMENT, desig2, project2));
        folderRepository.save(new Folder("Design Description", FolderTypes.DOCUMENT, desig2, project2));
        folderRepository.save(new Folder("System Main ICD", FolderTypes.DOCUMENT, desig2, project2));

        folderRepository.save(new Folder("Electrical System", FolderTypes.DOCUMENT, detal2, project2));
        folderRepository.save(new Folder("Communication System", FolderTypes.DOCUMENT, detal2, project2));
        folderRepository.save(new Folder("Subsystem #1", FolderTypes.DOCUMENT, detal2, project2));

        folderRepository.save(new Folder("General Conditions", FolderTypes.DOCUMENT, contract2, project2));
        folderRepository.save(new Folder("Appendices", FolderTypes.DOCUMENT, contract2, project2));

        folderRepository.save(new Folder("Requirements Lists", FolderTypes.DOCUMENT, internal2, project2));
        folderRepository.save(new Folder("Design Standard", FolderTypes.DOCUMENT, internal2, project2));

        folderRepository.save(new Folder("EN Standards", FolderTypes.DOCUMENT, thirdpty2, project2));
        folderRepository.save(new Folder("Railway Safety Board", FolderTypes.DOCUMENT, thirdpty2, project2));
        folderRepository.save(new Folder("Electricity Authority", FolderTypes.DOCUMENT, thirdpty2, project2));
        folderRepository.save(new Folder("Depart. of Transport", FolderTypes.DOCUMENT, thirdpty2, project2));

        companyRepository.findAll().forEach(System.out::println);
        projectRepository.findAll().forEach(System.out::println);
        folderRepository.findAll().forEach(System.out::println);

    }

    @Transactional
    private void SeedArtifactTypes() {

        artifactTypeRepository.save(new HashSet<ArtifactType>() {
            {

                add(new ArtifactType("GEN", "Generic Project Documennt", "This document type is for a generic project document that does not fall under specifications. It may or may not contain requirements. Plans, reports, procudures etc. fall under this category."));
                add(new ArtifactType("RLS", "Requirements List", "This document is a preliminary collection of system requirements that are discovered from interection with stakeholders. The requirements act as an input to the formal SyRS."));
                add(new ArtifactType("URS", "User Requirements Specification", "A User Requirements Specification (a.k.a. Stakeholder Requirements Specification) specifies the requirements the user expects from the system to be constructed. User Requirements are the top level of requirements. They capture the needs of users, the customer and other sources of requirements like legal regulations and internal company high level requirements."));
                add(new ArtifactType("SyRS", "System Requirements Specification", "A System Requirements Specification contains the next level of requirements after user requirements. The aim of system requirements is to set precise technical requirements for the system development. System requirements are derived from user requirements by considering existing technology, components and so on."));
                add(new ArtifactType("VRS", "Verification Requirements Specification", "The Verification Requirements Specification (VRS) describes the qualities of the evidence required that a set of requirements defining an item is satisfied. The item may be of any nature whatsoever, ranging from, for example, a physical object, to software, to an interface, to a data item, to a material, or to a service."));
                add(new ArtifactType("SyDD", "System Design Description", "The System/Subsystem Design Description (aka SSDD) describes the system- or subsystem-wide design and the architectural design of a system or subsystem. The SyDD may be supplemented by Interface Design Descriptions  and Database Design Descriptions."));
                add(new ArtifactType("IRS", "Interface Requirements Specification", "Interface Requirements Specification (IRS) specifies the requirements imposed on one or more systems, subsystems, Hardware Configuration Items (HWCIs), Computer Software Configuration Items (CSCIs), manual operations, or other system components to achieve one or more interfaces among these entities."));
                add(new ArtifactType("ICD", "Interface Control Document", "Interface Control Document (ICD) is a document that describes the interface(s) to a system or subsystem. It may describe the inputs and outputs of a single system or the interface between two systems or subsystems. It can be very detailed or pretty high level, but the point is to describe all inputs to and outputs from a system"));
                add(new ArtifactType("SRS", "Software Requirement Specification", "A software requirements specification (SRS) is a description of a software system to be developed. It lays out functional and non-functional requirements, and may include a set of use cases that describe user interactions that the software must provide."));
                add(new ArtifactType("SDD", "Software Design Description", "A software design description (aka software design document or SDD) is a written description of a software product, that a software designer writes in order to give a software development team overall guidance to the architecture of the software project."));

            }
        });

        artifactTypeRepository.findAll().forEach(System.out::println);
    }

    @Transactional
    private void SeedItemTypes() {

        itemTypeRepository.save(new HashSet<ItemType>() {
            {

                add(new ItemType("Unclassified", "Unclassified Requirement", "Requirements not classified."));
                add(new ItemType("Prose", "Prose Textual", "Prose textual information for clarification and context setting for a set of requirements."));
                add(new ItemType("Functional", "Functional Requirement", "A functional requirement describes a function of the system, i.e. what the system must do resp. a service the system provides to its users."));
                add(new ItemType("Performance", "Performance Requirement", "A performance requirement is a non-functional requirement that describes the amount of useful work accomplished by the system compared to the time and resources used."));
                add(new ItemType("Scalability", "Scalability Requirement", "A scalability requirement is a non-functional requirement that describes the ability of the system to handle growing amounts of work in a graceful manner or its ability to be enlarged to accommodate that growth."));
                add(new ItemType("Security", "Security Requirement", "A security requirement is a non-functional requirement that describes the stipulated degree of the systems protection against danger, damage, misuse, unauthorized access, data loss and crime."));
                add(new ItemType("Maintainability", "Maintainability Requirement", "A maintainability requirement is a non-functional requirement that describes the ease with which the system can be maintained in order to isolate defects or their cause, correct defects or their cause, meet new requirements, make future maintenance easier, or cope with a changed environment."));
                add(new ItemType("Usability", "Usability Requirement", "A usability requirement is a non-functional requirement describing the intended ease of use (ergonomical comfort) and learnability of the system."));
                add(new ItemType("Legal", "Legal Requirement", "A legal requirement is a non-functional requirement that states a regulation that must be recognized by the system. Regulations could be laws, standards, specifications, etc."));
                add(new ItemType("Story", "User Story", "A (user) story is a special kind of functional requirement, which uses one or more sentences in the everyday or business language of the end user that captures what the user (resp. a role) wants to achieve. User stories generally follow the following template: \"As a <role>, I want <goal/desire> so that <benefit>.\""));
                add(new ItemType("Constraint", "Constraint Requirement", "onstraint Requirement describes real-world limits or boundaries around what we want to happen"));
                add(new ItemType("Design", "Design Requirement", "Design requirements direct the design (internals of the system), by inclusion (build it this way), or exclusion (don't build it this way)."));

            }
        });

        itemTypeRepository.findAll().forEach(System.out::println);
    }

    @Transactional
    private void SeedSystemVariable() {

        Iterable<Artifact> artifacts = artifactRepository.findAll();

        for (Artifact artifact : artifacts) {

            String uuidTemplate = artifact.getArtifactName().substring(0, 3).toUpperCase() + "-ID" + String.format("%02d", artifact.getId());
            String reqIdTemplate = artifact.getArtifactName().substring(0, 2).toUpperCase() + String.format("%02d", artifact.getId());

            sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", artifact.getId()));

            sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_TEMPLATE.name(), uuidTemplate, "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-GENL", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-FCNL", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-ENVR", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-INTF", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-QLTY", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-SAFT", "DOCUMENT", artifact.getId()));
            sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), reqIdTemplate + "-STAT", "DOCUMENT", artifact.getId()));

        }
    }
}
