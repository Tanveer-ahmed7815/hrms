package com.te.flinko.handler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.te.flinko.exception.AdvanceSalaryNotAppliedException;
import com.te.flinko.exception.ArrayEmptyException;
import com.te.flinko.exception.ComapnyNameAlreadyExistsException;
import com.te.flinko.exception.CompanyIdNotFoundException;
import com.te.flinko.exception.DataNotFoundException;
import com.te.flinko.exception.DuplicateDesignationException;
import com.te.flinko.exception.EmployeeTimeSheetCannottEditedException;
import com.te.flinko.exception.EventCannotBeEditedException;
import com.te.flinko.exception.EventDetailsNotFoundException;
import com.te.flinko.exception.EventNotFoundException;
import com.te.flinko.exception.FailedToUploadException;
import com.te.flinko.exception.FormInformationNotFilledException;
import com.te.flinko.exception.InavlidInputException;
import com.te.flinko.exception.LeaveIdNotFoundException;
import com.te.flinko.exception.PermissionDeniedException;
import com.te.flinko.exception.ReimbursementNotAppliedException;
import com.te.flinko.exception.ReportingIdNotFoundException;
import com.te.flinko.exception.ScheduleInterviewDetailsNotFound;
import com.te.flinko.exception.SoftwareAlreadyAvailableException;
import com.te.flinko.exception.StatusNotAvailableException;
import com.te.flinko.exception.TaskIdNotFoundException;
import com.te.flinko.exception.TerminalIdNotFoundException;
import com.te.flinko.exception.UserNotFoundException;
import com.te.flinko.exception.admin.BranchNotFoundException;
import com.te.flinko.exception.admin.CategoryChildPresentException;
import com.te.flinko.exception.admin.CompanyNotFound;
import com.te.flinko.exception.admin.DesignationCannotUpdate;
import com.te.flinko.exception.admin.DuplicateColorException;
import com.te.flinko.exception.admin.DuplicateCompanyStockCategoriesNameException;
import com.te.flinko.exception.admin.DuplicateEmployeeIdException;
import com.te.flinko.exception.admin.DuplicateExpenseException;
import com.te.flinko.exception.admin.DuplicateLeadException;
import com.te.flinko.exception.admin.DuplicateLeadNameException;
import com.te.flinko.exception.admin.DuplicateShiftNameException;
import com.te.flinko.exception.admin.DuplicateStockGroupNameException;
import com.te.flinko.exception.admin.DuplicateStockUnitSymbolException;
import com.te.flinko.exception.admin.DuplicateofficialEmailIdException;
import com.te.flinko.exception.admin.ExpenseNotUpdatedException;
import com.te.flinko.exception.admin.HolidayNotFoundException;
import com.te.flinko.exception.admin.IsSubmittedException;
import com.te.flinko.exception.admin.LeadNotUpdatedException;
import com.te.flinko.exception.admin.LevelsOfApprovalAlreadyExistException;
import com.te.flinko.exception.admin.NoCompanyPresentException;
import com.te.flinko.exception.admin.NoDataPresentException;
import com.te.flinko.exception.admin.NoDeleteWorkWeekRuleException;
import com.te.flinko.exception.admin.NoEmployeeOfficialInfoException;
import com.te.flinko.exception.admin.NoEmployeePresentException;
import com.te.flinko.exception.admin.NoPayrollAvailableException;
import com.te.flinko.exception.admin.NoPersonalInfoException;
import com.te.flinko.exception.admin.NoTicketFoundException;
import com.te.flinko.exception.admin.NoWorkWeekRuleException;
import com.te.flinko.exception.admin.PayrollDeductionTitleSameException;
import com.te.flinko.exception.admin.PayrollEarningSalaryComponentSameException;
import com.te.flinko.exception.admin.RuleNameDefaultException;
import com.te.flinko.exception.admin.RuleNameExitException;
import com.te.flinko.exception.admin.RuleNameSameException;
import com.te.flinko.exception.admin.StockCategoryNotFoundException;
import com.te.flinko.exception.admin.StockGroupNotFound;
import com.te.flinko.exception.admin.StockUnitNotFoundException;
import com.te.flinko.exception.admin.WorkWeekAssociateWithEmpException;
import com.te.flinko.exception.admin.WorkWeekRuleNotFoundException;
import com.te.flinko.exception.admindept.CompanyPCLaptopDetailsAlreadyPresentException;
import com.te.flinko.exception.admindept.CompanyPCLaptopDetailsNotFoundException;
import com.te.flinko.exception.admindept.DuplicateProductNameException;
import com.te.flinko.exception.admindept.EmployeeListNotFoundException;
import com.te.flinko.exception.admindept.InOutNotSelectedException;
import com.te.flinko.exception.admindept.NoHardwarePresentException;
import com.te.flinko.exception.admindept.PurchaseOrderDoesNotExistsException;
import com.te.flinko.exception.admindept.PurchaseOrderItemNotFoundException;
import com.te.flinko.exception.admindept.SalesOrderDoesNotExistsException;
import com.te.flinko.exception.admindept.SalesOrderItemNotFoundException;
import com.te.flinko.exception.employee.EmployeeLoginException;
import com.te.flinko.exception.employee.EmployeeNotFoundException;
import com.te.flinko.exception.employee.EmployeeNotRegisteredException;
import com.te.flinko.exception.employee.InsufficientLeavesException;
import com.te.flinko.exception.employee.MessagingException;
import com.te.flinko.exception.employee.OTPNotFoundExpireException;
import com.te.flinko.exception.hr.CompanyNotFoundException;
import com.te.flinko.exception.hr.CustomExceptionForHr;
import com.te.flinko.exception.hr.OfficalIdInUseException;
import com.te.flinko.response.ErrorResponse;
import com.te.flinko.response.SuccessResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	
	@ExceptionHandler(value = OfficalIdInUseException.class)
	public ResponseEntity<SuccessResponse> officialIdInUseExceptionHandler(OfficalIdInUseException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
 
	@ExceptionHandler(value = UserNotFoundException.class)
	public ResponseEntity<SuccessResponse> userNotFoundExceptionHandler(UserNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), "user info not present"),
				HttpStatus.BAD_REQUEST);
	}

	
	@ExceptionHandler(value = NoHardwarePresentException.class)
	public ResponseEntity<SuccessResponse> noHardwarePresentExceptionHandler(NoHardwarePresentException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), "user info not present"),
				HttpStatus.BAD_REQUEST);
	}
	
	

	@ExceptionHandler(value = TerminalIdNotFoundException.class)
	public ResponseEntity<SuccessResponse> terminalIdNotFoundExceptionHandler(TerminalIdNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = FormInformationNotFilledException.class)
	public ResponseEntity<SuccessResponse> formInformationNotFilledExceptionHandler(
			FormInformationNotFilledException exception) {
		return new ResponseEntity<SuccessResponse>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ReimbursementNotAppliedException.class)
	public ResponseEntity<SuccessResponse> reimbursementNotAppliedExceptionHandler(
			ReimbursementNotAppliedException exception) {
		return new ResponseEntity<SuccessResponse>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = AdvanceSalaryNotAppliedException.class)
	public ResponseEntity<SuccessResponse> advanceSalaryNotAppliedExceptionHandler(
			AdvanceSalaryNotAppliedException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = NoTicketFoundException.class)
	public ResponseEntity<SuccessResponse> noTicketFoundException(NoTicketFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = DuplicateShiftNameException.class)
	public ResponseEntity<SuccessResponse> duplicateShiftNameException(DuplicateShiftNameException exception) {
		return new ResponseEntity<SuccessResponse>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = EmployeeTimeSheetCannottEditedException.class)
	public ResponseEntity<SuccessResponse> employeeTimeSheetCannottEditedException(
			EmployeeTimeSheetCannottEditedException exception) {
		return new ResponseEntity<SuccessResponse>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = RuleNameExitException.class)
	public ResponseEntity<SuccessResponse> ruleNameExitException(RuleNameExitException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EventDetailsNotFoundException.class)
	public ResponseEntity<SuccessResponse> eventDetailsNotFoundExceptionHandler(
			EventDetailsNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateDesignationException.class)
	public ResponseEntity<SuccessResponse> duplicateDesignationException(DuplicateDesignationException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoWorkWeekRuleException.class)
	public ResponseEntity<SuccessResponse> noWorkWeekRuleException(NoWorkWeekRuleException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EventNotFoundException.class)
	public ResponseEntity<SuccessResponse> eventNotFoundExceptionHandler(EventNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = RuleNameSameException.class)
	public ResponseEntity<SuccessResponse> ruleNameSameException(RuleNameSameException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EventCannotBeEditedException.class)
	public ResponseEntity<SuccessResponse> eventCannotBeEditedException(EventCannotBeEditedException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoDeleteWorkWeekRuleException.class)
	public ResponseEntity<SuccessResponse> noDeleteWorkWeekRule(NoDeleteWorkWeekRuleException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ScheduleInterviewDetailsNotFound.class)
	public ResponseEntity<SuccessResponse> scheduleInterviewDetailsNotFound(
			ScheduleInterviewDetailsNotFound exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoEmployeePresentException.class)
	public ResponseEntity<SuccessResponse> noEmployeePresent(NoEmployeePresentException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DataNotFoundException.class)
	public ResponseEntity<SuccessResponse> dataNotFoundException(DataNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = com.te.flinko.exception.employee.DataNotFoundException.class)
	public ResponseEntity<SuccessResponse> customDataNotFoundException(
			com.te.flinko.exception.employee.DataNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoCompanyPresentException.class)
	public ResponseEntity<SuccessResponse> noEmployeePresent(NoCompanyPresentException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = NoDataPresentException.class)
	public ResponseEntity<SuccessResponse> noDataPresentException(NoDataPresentException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ComapnyNameAlreadyExistsException.class)
	public ResponseEntity<SuccessResponse> comapnyNameAlreadyExistsException(
			ComapnyNameAlreadyExistsException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DesignationCannotUpdate.class)
	public ResponseEntity<SuccessResponse> designationCannotUpdate(DesignationCannotUpdate exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = LeadNotUpdatedException.class)
	public ResponseEntity<ErrorResponse> leadNotUpdatedException(LeadNotUpdatedException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = DuplicateExpenseException.class)
	public ResponseEntity<ErrorResponse> duplicateExpenseException(DuplicateExpenseException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = ExpenseNotUpdatedException.class)
	public ResponseEntity<ErrorResponse> expenseNotUpdatedException(ExpenseNotUpdatedException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = DuplicateLeadException.class)
	public ResponseEntity<SuccessResponse> duplicateLeadException(DuplicateLeadException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateLeadNameException.class)
	public ResponseEntity<SuccessResponse> duplicateLeadNameException(DuplicateLeadNameException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateColorException.class)
	public ResponseEntity<SuccessResponse> duplicateColorException(DuplicateColorException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<SuccessResponse> noDataPresent(NoDataPresentException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = NoPersonalInfoException.class)
	public ResponseEntity<SuccessResponse> noPersonalInfo(NoPersonalInfoException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = WorkWeekAssociateWithEmpException.class)
	public ResponseEntity<SuccessResponse> workWeekAssociateWithEmployee(WorkWeekAssociateWithEmpException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.FAILED_DEPENDENCY);
	}

	@ExceptionHandler(value = NoEmployeeOfficialInfoException.class)
	public ResponseEntity<SuccessResponse> noEmployeeOfficialInfo(NoEmployeeOfficialInfoException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = NoPayrollAvailableException.class)
	public ResponseEntity<SuccessResponse> noPayrollAvailable(NoPayrollAvailableException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = RuleNameDefaultException.class)
	public ResponseEntity<SuccessResponse> ruleNameDefault(RuleNameDefaultException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PermissionDeniedException.class)
	public ResponseEntity<SuccessResponse> permissionDeniedException(PermissionDeniedException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PayrollEarningSalaryComponentSameException.class)
	public ResponseEntity<SuccessResponse> payrollEarningSalaryComponentSame(
			PayrollEarningSalaryComponentSameException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	public ResponseEntity<SuccessResponse> payrollDeductionTitlesame(PayrollDeductionTitleSameException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CategoryChildPresentException.class)
	public ResponseEntity<SuccessResponse> categoryChildPresentException(CategoryChildPresentException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CompanyNotFound.class)
	public ResponseEntity<SuccessResponse> companyNotFound(CompanyNotFound exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CompanyIdNotFoundException.class)
	public ResponseEntity<SuccessResponse> companyIdNotFoundException(CompanyIdNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateCompanyStockCategoriesNameException.class)
	public ResponseEntity<SuccessResponse> duplicateCompanyStockCategoriesNameException(
			DuplicateCompanyStockCategoriesNameException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateStockGroupNameException.class)
	public ResponseEntity<SuccessResponse> duplicateStockGroupNameException(
			DuplicateStockGroupNameException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateStockUnitSymbolException.class)
	public ResponseEntity<SuccessResponse> duplicateStockUnitSymbolException(
			DuplicateStockUnitSymbolException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = IsSubmittedException.class)
	public ResponseEntity<SuccessResponse> isSubmittedException(IsSubmittedException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StockCategoryNotFoundException.class)
	public ResponseEntity<SuccessResponse> stockCategoryNotFoundException(StockCategoryNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StockGroupNotFound.class)
	public ResponseEntity<SuccessResponse> stockGroupNotFound(StockGroupNotFound exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StockUnitNotFoundException.class)
	public ResponseEntity<SuccessResponse> stockUnitNotFoundException(StockUnitNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = WorkWeekRuleNotFoundException.class)
	public ResponseEntity<SuccessResponse> workWeekRuleNotFoundException(WorkWeekRuleNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateofficialEmailIdException.class)
	public ResponseEntity<SuccessResponse> duplicateofficialEmailIdException(
			DuplicateofficialEmailIdException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = DuplicateEmployeeIdException.class)
	public ResponseEntity<SuccessResponse> duplicateEmployeeIdException(DuplicateEmployeeIdException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = HolidayNotFoundException.class)
	public ResponseEntity<SuccessResponse> holidayNotFoundException(HolidayNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = BranchNotFoundException.class)
	public ResponseEntity<SuccessResponse> branchNotFoundException(BranchNotFoundException exception) {

		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CompanyNotFoundException.class)
	public ResponseEntity<SuccessResponse> companyNotFoundException(CompanyNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = CustomExceptionForHr.class)
	public ResponseEntity<SuccessResponse> customExceptionForHr(CustomExceptionForHr exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(NoSuchElementException.class)
	public ResponseEntity<SuccessResponse> noSuchElementException(NoSuchElementException ex) {
		SuccessResponse successResponse = new SuccessResponse(true, "No such element is present in database", null);
		log.error("try to find information which is not present in Database");
		return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<SuccessResponse> constraintViolationException(ConstraintViolationException ex) {
		SuccessResponse successResponse = new SuccessResponse(true, "duplicate entry found", ex.getLocalizedMessage());
		log.error("duplicate entry found");
		return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(SQLIntegrityConstraintViolationException.class)
	public ResponseEntity<SuccessResponse> sqlIntegrityConstraintViolationException(
			SQLIntegrityConstraintViolationException ex) {
		log.error("duplicate entry");
		return new ResponseEntity<>(new SuccessResponse(true, ex.getLocalizedMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(IndexOutOfBoundsException.class)
	public ResponseEntity<SuccessResponse> indexOutOfBoundsException(IndexOutOfBoundsException ex) {
		log.error("index out of bound exception ");
		return new ResponseEntity<>(new SuccessResponse(true, ex.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = MessagingException.class)
	public ResponseEntity<ErrorResponse> messagingExceptionHandler(MessagingException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = OTPNotFoundExpireException.class)
	public ResponseEntity<ErrorResponse> oTPNotFoundExpireExceptionHandler(OTPNotFoundExpireException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = EmployeeNotFoundException.class)
	public ResponseEntity<ErrorResponse> employeeNotFoundExceptionHandler(EmployeeNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = LevelsOfApprovalAlreadyExistException.class)
	public ResponseEntity<ErrorResponse> levelsOfApprovalAlreadyExistException(
			LevelsOfApprovalAlreadyExistException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = EmployeeNotRegisteredException.class)
	public ResponseEntity<ErrorResponse> employeeNotRegisteredException(EmployeeNotRegisteredException exception) {
		return ResponseEntity.status(HttpStatus.OK)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = EmployeeLoginException.class)
	public ResponseEntity<ErrorResponse> employeeLoginExceptionHandler(EmployeeLoginException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	// handleHttpMediaTypeNotSupported : triggers when the JSON is invalid
	@Override
	protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		List<String> details = new ArrayList<>();
		StringBuilder builder = new StringBuilder();
		builder.append(ex.getContentType());
		builder.append(" media type is not supported. Supported media types are ");
		ex.getSupportedMediaTypes().forEach(t -> builder.append(t).append(", "));
		details.add(builder.toString());
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(details).build());

	}

	// handleHttpMessageNotReadable : triggers when the JSON is malformed
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
//		String[] errMsg = ex.getMessage().split(":");
//		String string = errMsg[errMsg.length - 4];
//		errMsg[0] + " : " + errMsg[1] + string.substring(0, string.length() - 7) + ")"
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message("Something Went Wrong").build());
	}

	// handleMethodArgumentNotValid : triggers when @Valid fails
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true)
				.message(ex.getBindingResult().getFieldErrors().stream()
						.map(error -> error.getField() + " : " + error.getDefaultMessage())
						.collect(Collectors.toList()))
				.build());
	}

	// handleMissingServletRequestParameter : triggers when there are missing
	// parameters
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(
				ErrorResponse.builder().error(true).message(ex.getParameterName() + " parameter is missing").build());
	}

	// handleMethodArgumentTypeMismatch : triggers when a parameter's type does not
	// match
	@ExceptionHandler(MethodArgumentTypeMismatchException.class)
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
			WebRequest request) {

		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(ex.getMessage()).build());
	}

	// dataIntegrityViolationException : triggers when @Validated fails
	@ExceptionHandler({ DataIntegrityViolationException.class })
	public ResponseEntity<ErrorResponse> dataIntegrityViolationException(DataIntegrityViolationException exception,
			WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true)
				.message(exception.getMostSpecificCause().getMessage().split("for")[0]).build());
	}

	// handleResourceNotFoundException : triggers when there is not resource with
	// the specified ID in BDD
//	@ExceptionHandler(ResourceNotFoundException.class)
//	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex) {
//		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(ex.getMessage()).build());
//	}

	// handleNoHandlerFoundException : triggers when the handler method is invalid
	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		return ResponseEntity.ok(ErrorResponse.builder().error(true).message(
				String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()))
				.build());
	}

	@ExceptionHandler(FailedToUploadException.class)
	public ResponseEntity<SuccessResponse> failedToUploadException(FailedToUploadException failedToUploadException) {
		SuccessResponse successResponse = new SuccessResponse(true, failedToUploadException.getMessage(),
				failedToUploadException.getLocalizedMessage());
		log.error(failedToUploadException.getMessage());
		return new ResponseEntity<>(successResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = PurchaseOrderDoesNotExistsException.class)
	public ResponseEntity<ErrorResponse> purchaseOrderDoesNotExistsException(
			PurchaseOrderDoesNotExistsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = SalesOrderDoesNotExistsException.class)
	public ResponseEntity<ErrorResponse> salesOrderDoesNotExistsException(SalesOrderDoesNotExistsException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = InOutNotSelectedException.class)
	public ResponseEntity<ErrorResponse> inOutNotSelectedException(InOutNotSelectedException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = PurchaseOrderItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> purchaseOrderItemNotFoundException(
			PurchaseOrderItemNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = SalesOrderItemNotFoundException.class)
	public ResponseEntity<ErrorResponse> salesOrderItemNotFoundException(SalesOrderItemNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = CompanyPCLaptopDetailsAlreadyPresentException.class)
	public ResponseEntity<ErrorResponse> companyPCLaptopDetailsAlreadyPresentException(
			CompanyPCLaptopDetailsAlreadyPresentException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = DuplicateProductNameException.class)
	public ResponseEntity<ErrorResponse> duplicateProductNameException(DuplicateProductNameException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = CompanyPCLaptopDetailsNotFoundException.class)
	public ResponseEntity<ErrorResponse> companypCLaptopDetailsNotFoundException(
			CompanyPCLaptopDetailsNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = EmployeeListNotFoundException.class)
	public ResponseEntity<ErrorResponse> employeeListNotFoundException(EmployeeListNotFoundException exception) {
		return ResponseEntity.status(HttpStatus.BAD_REQUEST)
				.body(ErrorResponse.builder().error(true).message(exception.getMessage()).build());
	}

	@ExceptionHandler(value = LeaveIdNotFoundException.class)
	public ResponseEntity<SuccessResponse> leaveIdExceptionHandler(LeaveIdNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ReportingIdNotFoundException.class)
	public ResponseEntity<SuccessResponse> reportingIdExceptionHandler(ReportingIdNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = StatusNotAvailableException.class)
	public ResponseEntity<SuccessResponse> statusExceptionHandler(StatusNotAvailableException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = TaskIdNotFoundException.class)
	public ResponseEntity<SuccessResponse> taskExceptionHandler(TaskIdNotFoundException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = ArrayEmptyException.class)
	public ResponseEntity<SuccessResponse> arrayEmptyHandler(ArrayEmptyException emptyException) {
		return new ResponseEntity<>(new SuccessResponse(true, emptyException.getMessage(), null),
				HttpStatus.BAD_REQUEST);
	}
	
	@ExceptionHandler(value = InsufficientLeavesException.class)
	public ResponseEntity<SuccessResponse> insufficientLeavesExceptionHandler(
			InsufficientLeavesException exception) {
		return new ResponseEntity<>(new SuccessResponse(true, exception.getMessage(), null),
				HttpStatus.NOT_ACCEPTABLE);
	}
	
	@ExceptionHandler(value = InavlidInputException.class)
	public ResponseEntity<SuccessResponse> invalidInputExceptionHandler(InavlidInputException emptyException) {
		return new ResponseEntity<>(new SuccessResponse(true, emptyException.getMessage(), null),
				HttpStatus.BAD_REQUEST);

	}

	@ExceptionHandler(value = SoftwareAlreadyAvailableException.class)
	public ResponseEntity<SuccessResponse> softwareAlreadyAvailableException(SoftwareAlreadyAvailableException emptyException) {
		return new ResponseEntity<>(new SuccessResponse(true, emptyException.getMessage(), null),
				HttpStatus.BAD_REQUEST);

	}
	
	
}
