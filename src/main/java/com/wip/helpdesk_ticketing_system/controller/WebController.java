package com.wip.helpdesk_ticketing_system.controller;

import com.wip.helpdesk_ticketing_system.dto.AssignmentDto;
import com.wip.helpdesk_ticketing_system.dto.ResolutionDto;
import com.wip.helpdesk_ticketing_system.dto.TicketDto;
import com.wip.helpdesk_ticketing_system.dto.UserDto;
import com.wip.helpdesk_ticketing_system.entity.Ticket;
import com.wip.helpdesk_ticketing_system.entity.User;
import com.wip.helpdesk_ticketing_system.enums.Priority;
import com.wip.helpdesk_ticketing_system.enums.Role;
import com.wip.helpdesk_ticketing_system.enums.Status;
import com.wip.helpdesk_ticketing_system.repository.UserRepository;
import com.wip.helpdesk_ticketing_system.sevice.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class WebController {

    @Autowired private TicketService ticketService;
    @Autowired private UserService userService;
    @Autowired private AssignmentService assignmentService;
    @Autowired private ResolutionService resolutionService;
    @Autowired private UserRepository userRepository;

    // ===== AUTH =====
    @GetMapping("/login")
    public String loginPage(@RequestParam(required = false) String error,
                             @RequestParam(required = false) String logout,
                             @RequestParam(required = false) String expired,
                             Model model) {
        if (error != null) model.addAttribute("errorMsg", "Invalid email or password.");
        if (logout != null) model.addAttribute("logoutMsg", "You have been logged out.");
        if (expired != null) model.addAttribute("errorMsg", "Your session has expired.");
        return "login";
    }

    // ===== DASHBOARD =====
    @GetMapping({"/", "/dashboard"})
    public String dashboard(Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAgent = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENT"));
        boolean isEndUser = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_END_USER"));

        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);

        if (isAdmin) {
            List<Ticket> tickets = ticketService.getAllTickets();
            model.addAttribute("totalTickets", tickets.size());
            model.addAttribute("openTickets", tickets.stream().filter(t -> t.getStatus() == Status.OPEN).count());
            model.addAttribute("inProgressTickets", tickets.stream().filter(t -> t.getStatus() == Status.IN_PROGRESS).count());
            model.addAttribute("resolvedTickets", tickets.stream().filter(t -> t.getStatus() == Status.RESOLVED).count());
            model.addAttribute("closedTickets", tickets.stream().filter(t -> t.getStatus() == Status.CLOSED).count());
            model.addAttribute("totalUsers", userService.getAllUsers().size());
            model.addAttribute("recentTickets", tickets.stream().limit(5).toList());
        } else if (isAgent) {
            List<Ticket> allTickets = ticketService.getAllTickets();
            List<Ticket> myTickets = allTickets.stream()
                    .filter(t -> t.getAssignment() != null && 
                                 currentUser != null &&
                                 t.getAssignment().getAssignedTo() != null &&
                                 t.getAssignment().getAssignedTo().getUserId().equals(currentUser.getUserId()))
                    .toList();
            model.addAttribute("myTickets", myTickets.size());
            model.addAttribute("openTickets", myTickets.stream().filter(t -> t.getStatus() == Status.OPEN).count());
            model.addAttribute("inProgressTickets", myTickets.stream().filter(t -> t.getStatus() == Status.IN_PROGRESS).count());
            model.addAttribute("resolvedTickets", myTickets.stream().filter(t -> t.getStatus() == Status.RESOLVED).count());
            model.addAttribute("recentTickets", myTickets.stream().limit(5).toList());
        } else {
            // END_USER
            List<Ticket> userTickets = currentUser != null ?
                    ticketService.getTicketsByUser(currentUser.getUserId()) : List.of();
            model.addAttribute("totalTickets", userTickets.size());
            model.addAttribute("openTickets", userTickets.stream().filter(t -> t.getStatus() == Status.OPEN).count());
            model.addAttribute("inProgressTickets", userTickets.stream().filter(t -> t.getStatus() == Status.IN_PROGRESS).count());
            model.addAttribute("resolvedTickets", userTickets.stream().filter(t -> t.getStatus() == Status.RESOLVED).count());
            model.addAttribute("recentTickets", userTickets.stream().limit(5).toList());
        }

        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAgent", isAgent);
        model.addAttribute("isEndUser", isEndUser);
        model.addAttribute("currentUser", currentUser);
        return "dashboard";
    }

    // ===== TICKETS =====
    @GetMapping("/tickets")
    public String listTickets(Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAgent = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENT"));
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);

        List<Ticket> tickets;
        if (isAdmin || isAgent) {
            tickets = ticketService.getAllTickets();
        } else {
            tickets = currentUser != null ? ticketService.getTicketsByUser(currentUser.getUserId()) : List.of();
        }

        model.addAttribute("tickets", tickets);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAgent", isAgent);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("priorities", Priority.values());
        return "tickets/list";
    }

    @GetMapping("/tickets/new")
    public String newTicketForm(Model model, Authentication auth) {
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));

        model.addAttribute("ticketDto", new TicketDto());
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("isAdmin", isAdmin);
        if (isAdmin) {
            model.addAttribute("users", userService.getAllUsers());
        }
        return "tickets/form";
    }

    @PostMapping("/tickets/save")
    public String saveTicket(@ModelAttribute TicketDto dto,
                              Authentication auth,
                              RedirectAttributes ra) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        if (!isAdmin) {
            String email = auth.getName();
            User currentUser = userRepository.findByEmail(email).orElse(null);
            if (currentUser != null) dto.setUserId(currentUser.getUserId());
        }
        try {
            ticketService.createTicket(dto);
            ra.addFlashAttribute("success", "Ticket created successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error creating ticket: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    @GetMapping("/tickets/{id}")
    public String viewTicket(@PathVariable Long id, Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        boolean isAgent = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_AGENT"));
        Ticket ticket = ticketService.getTicketById(id);
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);

        model.addAttribute("ticket", ticket);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("isAgent", isAgent);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("statuses", Status.values());
        model.addAttribute("agents", userRepository.findByRole(Role.AGENT));
        model.addAttribute("assignmentDto", new AssignmentDto());
        model.addAttribute("resolutionDto", new ResolutionDto());
        return "tickets/detail";
    }

    @GetMapping("/tickets/{id}/edit")
    public String editTicketForm(@PathVariable Long id, Model model, Authentication auth) {
        boolean isAdmin = auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"));
        Ticket ticket = ticketService.getTicketById(id);
        TicketDto dto = new TicketDto();
        dto.setTitle(ticket.getTitle());
        dto.setDescription(ticket.getDescription());
        dto.setPriority(ticket.getPriority());
        dto.setUserId(ticket.getUser().getUserId());

        model.addAttribute("ticketDto", dto);
        model.addAttribute("ticketId", id);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("isAdmin", isAdmin);
        if (isAdmin) model.addAttribute("users", userService.getAllUsers());
        return "tickets/edit";
    }

    @PostMapping("/tickets/{id}/update")
    public String updateTicket(@PathVariable Long id, @ModelAttribute TicketDto dto,
                                RedirectAttributes ra) {
        try {
            ticketService.updateTicket(id, dto);
            ra.addFlashAttribute("success", "Ticket updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/status")
    public String updateStatus(@PathVariable Long id, @RequestParam Status status,
                                RedirectAttributes ra) {
        try {
            ticketService.updateStatus(id, status);
            ra.addFlashAttribute("success", "Status updated to " + status);
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    @PostMapping("/tickets/{id}/delete")
    public String deleteTicket(@PathVariable Long id, RedirectAttributes ra) {
        try {
            ticketService.deleteTicket(id);
            ra.addFlashAttribute("success", "Ticket deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tickets";
    }

    // ===== ASSIGNMENTS =====
    @PostMapping("/tickets/{id}/assign")
    public String assignTicket(@PathVariable Long id, @ModelAttribute AssignmentDto dto,
                                RedirectAttributes ra) {
        dto.setTicketId(id);
        try {
            assignmentService.assignTicket(dto);
            ra.addFlashAttribute("success", "Ticket assigned successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    // ===== RESOLUTIONS =====
    @PostMapping("/tickets/{id}/resolve")
    public String resolveTicket(@PathVariable Long id, @ModelAttribute ResolutionDto dto,
                                 Authentication auth, RedirectAttributes ra) {
        dto.setTicketId(id);
        String email = auth.getName();
        User currentUser = userRepository.findByEmail(email).orElse(null);
        if (currentUser != null) dto.setUserId(currentUser.getUserId());
        try {
            resolutionService.resolveTicket(dto);
            ra.addFlashAttribute("success", "Ticket resolved successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/tickets/" + id;
    }

    // ===== ADMIN - USERS =====
    @GetMapping("/admin/users")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.getAllUsers());
        return "admin/users";
    }

    @GetMapping("/admin/users/new")
    public String newUserForm(Model model) {
        model.addAttribute("userDto", new UserDto());
        model.addAttribute("roles", Role.values());
        return "admin/user-form";
    }

    @PostMapping("/admin/users/save")
    public String saveUser(@ModelAttribute UserDto dto, RedirectAttributes ra) {
        try {
            userService.addUser(dto);
            ra.addFlashAttribute("success", "User created successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/{id}/edit")
    public String editUserForm(@PathVariable Long id, Model model) {
        User user = userService.getUserById(id);
        UserDto dto = new UserDto();
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setRole(user.getRole());
        model.addAttribute("userDto", dto);
        model.addAttribute("userId", id);
        model.addAttribute("roles", Role.values());
        return "admin/user-edit";
    }

    @PostMapping("/admin/users/{id}/update")
    public String updateUser(@PathVariable Long id, @ModelAttribute UserDto dto,
                              RedirectAttributes ra) {
        try {
            userService.updateUser(id, dto);
            ra.addFlashAttribute("success", "User updated successfully!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            userService.deleteUser(id);
            ra.addFlashAttribute("success", "User deleted.");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Error: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    // ===== ADMIN - ASSIGNMENTS =====
    @GetMapping("/admin/assignments")
    public String listAssignments(Model model) {
        model.addAttribute("assignments", assignmentService.getAllAssignments());
        return "admin/assignments";
    }

    // ===== ADMIN - RESOLUTIONS =====
    @GetMapping("/admin/resolutions")
    public String listResolutions(Model model) {
        model.addAttribute("resolutions", resolutionService.getAllResolutions());
        return "admin/resolutions";
    }
}
