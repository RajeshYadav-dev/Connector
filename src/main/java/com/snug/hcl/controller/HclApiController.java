
package com.snug.hcl.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snug.hcl.service.APIRequestService;
import com.snug.hcl.service.CreateAccountUnlockService;
import com.snug.hcl.service.CreatePasswordResetTicketService;
import com.snug.hcl.service.IncidentRequestService;
import com.snug.hcl.service.RequestTicketService;
import com.snug.hcl.service.APIGetLastTicketsService;
import com.snug.hcl.service.APIGetUserIDService;

@RestController
public class HclApiController {

	@Autowired
	private APIRequestService apiRequestService;

	@Autowired
	private IncidentRequestService incidentRequestService;

	@Autowired
	private RequestTicketService requestTicketService;

	@Autowired
	private APIGetUserIDService apiGetUserIDService;

	@Autowired
	private CreatePasswordResetTicketService createPasswordResetTicketService;

	@Autowired
	private CreateAccountUnlockService createAccountUnlockService;

	@GetMapping("/validate-user")
	public HashMap<String, Object> validateUserByEmpId(@RequestParam("empId") String empId) {
		return apiRequestService.validateUser(empId);
	}

	@GetMapping("/incident-status")
	public HashMap<String, Object> validateINCTicketStatus(@RequestParam("incident") String incident) {
		return incidentRequestService.incidentTicketStatus(incident);

	}

	@GetMapping("/request-status")
	public HashMap<String, Object> validateREQTicketStatus(@RequestParam("request") String request) {
		return requestTicketService.requestTicketStatus(request);
	}

	@GetMapping("/validate-token")
	public HashMap<String, String> findUserByEmail(@RequestParam("email") String email, @RequestParam("passcode") String passcode) {
		
		return apiGetUserIDService.validateOKTAToken(email, passcode);
	}
	
	@GetMapping("/validate-factor-token")
	public HashMap<String, String> validateTokenByFactor(@RequestParam("userId") String userId,@RequestParam("factorId") String factorId, @RequestParam("passcode") String passcode) {
		
		return apiGetUserIDService.validateFactorToken(userId, factorId,  passcode);
	}
	

	@PostMapping("/password-reset")
	public HashMap<String, Object> passwordReset(@RequestParam("email") String email) {
		return createPasswordResetTicketService.createPasswordResetTicket(email);
	}

	@PostMapping("/account-unlock")
	public HashMap<String, Object> accountUnlock(@RequestParam("email") String email) {
		return createAccountUnlockService.createAccountUnlockService(email);
	}
	
	
	@Autowired
	private APIGetLastTicketsService apiGetLastTicketsService;
	
	@GetMapping("/get-last-incidents")
	public HashMap<String, String> getLastIncidentsByEmpID(@RequestParam("empId") String empId) {
		
		return apiGetLastTicketsService.getLastIncidents(empId);
	}
	
	@GetMapping("/get-last-requests")
	public HashMap<String, String> getLastRequestsByEmpID(@RequestParam("empId") String empId) {
		
		return apiGetLastTicketsService.getLastRequests(empId);
	}
	
	@GetMapping("/get-last-tickets")
	public HashMap<String, Object> getLastTicketsByEmpID(@RequestParam("empId") String empId) {
		
		return apiGetLastTicketsService.getLastThreeTickets(empId);
	}
}
