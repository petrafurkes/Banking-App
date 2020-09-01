/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.bankapp.resources;

import com.google.gson.Gson;
import com.mycompany.bankapp.model.Account;
import com.mycompany.bankapp.model.Customer;
import com.mycompany.bankapp.model.Transaction;
import com.mycompany.bankapp.service.BankAppService;
import java.util.List;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 *
 * @author Petra Furkes
 * 17th August, 2020
 */
@Path("/customer")
public class BankAppResource {
    
   BankAppService customer = new BankAppService();
   
   
    //===================================  1. ADMIN API  ===============================================================
    //1.1. CREATE CUSTOMERS FROM MOCKUP DATA XML-----------------------------------------------------------------------
    //API name:create_mockup_customers_xml
    // After restarting or building this project...... MUST ALWAYS run the create URI
    //curl -vi -H "Accept: application/xml" -X GET -G "http://localhost:49000/api/customer/createMockCustomers"    
    @GET
    @Path("/createMockCustomers")
    @Produces(MediaType.APPLICATION_XML)
    public List<Customer> createCustomer(){
        return customer.createCustomer();
    }
    
    
    
    //1.2. CREATE CUSTOMERS FROM MOCK DATA JSON------------------------------------------------------------------------
    //API name:create_mockup_customers_json
    // After restarting or building this project...... MUST ALWAYS run the create URI
    //curl -vi -H "Accept: application/json" -X GET -G "http://localhost:49000/api/customer/createMockCustomers"    
    @GET
    @Path("/createMockCustomers")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCustomerJSON(){
        Gson gson = new Gson();
        List<Customer> cust = customer.createCustomer();
        return Response.status(Response.Status.CREATED).entity(gson.toJson(cust)).build();
    }
    
    
    //1.3.  DISPLAY ALL CUSTOMERS XML-----------------------------------------------------------------------------------
    //API name:display_customers_xml
    //to display all customers with full details
    //curl -vi -H "Accept: application/xml" -X GET -G "http://localhost:49000/api/customer/getAllC"    
    @GET
    @Path("/getAllC")
    @Produces(MediaType.APPLICATION_XML)
    public List<Customer> getAllCustomersXML(){
        return customer.getAllCustomers();
    } 
    
    //1.4.   DISPLAY ALL CUSTOMERS JSON--------------------------------------------------------------------------------
    //API name:display_customers_json
    //to display all customers with full details
    //curl -vi -H "Accept: application/json" -X GET -G "http://localhost:49000/api/customer/getAllC"        
    @GET
    @Path("/getAllC")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllCustomersJSON(){
        Gson gson = new Gson();
        List<Customer> cust = customer.getAllCustomers();
        return Response.status(Response.Status.CREATED).entity(gson.toJson(cust)).build();
    }   
    
    
    //1.5. CREATE NEW CUSTOMER IN POSTMAN ALL DATA IN THE BODY----------------------------------------------------------
    //API name: create_postman_customer
    /*
        {
            "customerId": 55,
            "customerName": "customer created by postman body",
            "customerAddress": "Street 25, Dublin 5, Ireland",
            "customerEmail": "test@mail.com",
            "password": "password555"
        }
    */
    
    // curl -v -X POST "http://localhost:49000/api/customer/createMC" 
    
    @POST
    @Path("/createMC")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewMockCustomer(String body){
        Gson gson = new Gson();
        Customer newCustomer = gson.fromJson(body, Customer.class);
        customer.addMockCustomer(newCustomer);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(newCustomer)).build();    
    }  
    
    
    
    //=================================  2. CUSTOMER API  ==============================================================
    //2.1. CUSTOMER REGISTRATION : registration form (email and password) ----------------------------------------------
    //API name:registration_form_
    //resources for customer on the fly(using web aplication), or can be used by postman with data in the postman body
    /*
    {
    
    "customerEmail": "test@mail.com",
    "password": "password555"
    
    }
    */
    //In Postman... "http://localhost:49000/api/customer" 
    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createNewCustomer(String body){
        Gson gson = new Gson();
        Customer newCustomer = gson.fromJson(body, Customer.class);
        customer.addCustomer(newCustomer);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(newCustomer)).build();    
    }  
    
    //2.2. CUSTOMER REGISTRATION CONTINUE: registration form (name and address) ----------------------------------------
    //API name:registration_form_continue
    //In Postman... http://localhost:49000/api/customer/{customerId}/update?customerName={customerName}&customeAddress={customeAddress}
    @PUT
    @Path("/{customerId}/update")
    @Produces(MediaType.APPLICATION_JSON)  
    public Response updateCustomer(@PathParam("customerId") int customerId, 
                               @QueryParam("customerName") String customerName, 
                               @QueryParam("customerAddress") String customerAddress){
        Gson gson = new Gson();
        customer.updateCustomer(customerId, customerName, customerAddress);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.getCustomer(customerId))).build();
        
    }   
    
    
    //2.3. GET CUSTOMER DETAILS BY CUSTOMER ID JSON---------------------------------------------------------------------
    //API name:display_all_customers
    //curl -vi -H "Accept: application/json" -X GET -G "http://localhost:49000/api/customer/getAC/{customerId}"    
    @GET
    @Path("/getAC/{customerId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getACustomersJSON(@PathParam("customerId") int customerId){
        Gson gson = new Gson();
        Customer cust = customer.getCustomer(customerId);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(cust)).build();        
    }  
    
    
    
    //2.4. CREATE ACCOUNT BY ACCOUNT TYPE AND BY CUSTOMER ID JSON-------------------------------------------------------
    //API name:create_card_account
    //"http://localhost:49000/api/customer/{customerId}/Account"
    //in Postman body = only accountType data, the rest is going to be overwritten because of method settings
    /*
    {
        "accountType": "Visa Debit"
    
    }
    */
    @POST
    @Path("/{customerId}/Account")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addNewAccountByCIDDataInBody(@PathParam("customerId")int customerId, String body){
        Gson gson = new Gson();
        Account newAccount = gson.fromJson(body,Account.class);
        customer.addAccountOnTheList(customerId, newAccount);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(newAccount)).build();    
        
    }  
    
    
    //2.5. DISPLAY ACCOUNT BY ACCOUNT TYPE AND BY CUSTOMER ID-----------------------------------------------------------
    //API name:display_account_by_accType
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/getUA/{accountType}")
    public Response getAccountByAccountType(@PathParam("customerId") int customerId, 
                                   @PathParam("accountType") String accountType){
        Gson gson = new Gson();
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.
                displayAccountByTypeAndByCustomerId(customerId, accountType))).build();
    }  
    
    
    //2.6.GET ALL ACCOUNTS BY CUSTOMER ID-------------------------------------------------------------------------------
    //USED IN CUSTOMER AND ADMIN APP
    //API name:display_all_customer_accounts
    // curl -vi -H "Accept: application/json" -X GET -G "http://localhost:49000/api/customer/{customerId}/getAllAccounts"
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/getAllAccounts")
    public Response getAccountsJSON(@PathParam("customerId") int customerId){
        Gson gson = new Gson();
       Customer cust = customer.getCustomer(customerId);
        List accounts = cust.getAccountList();
        return Response.status(Response.Status.CREATED).entity(gson.toJson(accounts)).build();
    }
    
    //2.7.GET ACCOUNT BALANCE BY ACCOUNT TYPE---------------------------------------------------------------------------
    //API name:account_balance
    // curl -vi -H "Accept: application/json" -X GET -G "http://localhost:49000/api/customer/{customerId}/account/{accType}/balance"
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/account/{accType}/balance")
    public Response getAccBalance(@PathParam("customerId") int customerId, 
                               @PathParam("accType") String accType ){
        Gson gson = new Gson();
        Double balance = customer.getBalance(customerId, accType);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(balance)).build();
    }    
    
    //2.8.TO CREATE A WITHDRAWAL TRANSACTION----------------------------------------------------------------------------
    //THE METHOD WILL RETURN A MESSAGE IF WITHDRAWAL TRANSACTION WAS SUCCESSFUL OR NOT. IF IT WAS SUCCESSFUL IT WILL RETURN 
    //ACCOUNT BALANCE IN MESSAGE, AS WELL IT WILL CREATE A TRANSACTION
    //TRANSACTION AMOUNT IS SENT IN THE BODY
    //API name:withdrawal
    // In Postman "http://localhost:49000/api/customer/{customerId}/transaction/{accType}/wid/{accNumber}/create"
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/transaction/{accType}/wid/{accNumber}/create")
    public Response createWithdrawalTransaction(@PathParam("customerId") int customerId, 
                                                                    @PathParam("accType") String accType, 
                                                                    @PathParam("accNumber") int accNumber, 
                                                                    String body){
        Gson gson = new Gson();
        Transaction newTransaction = gson.fromJson(body,Transaction.class);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.
                withdrawal(customerId, accType, accNumber, newTransaction))).build();
    }    
    
    
    //2.9. TO CREATE A LODGEMENT TRANSACTION----------------------------------------------------------------------------
    //THE METHOD WILL RETURN A MESSAGE IF LODGEMENT TRANSACTION WAS SUCCESSFUL OR NOT. IF IT WAS SUCCESSFUL IT WILL RETURN 
    //ACCOUNT BALANCE IN MESSAGE, AS WELL IT WILL CREATE A TRANSACTION
    //TRANSACTION AMOUNT IS SENT IN THE BODY
    //API name:lodgement
    // In Postman "http://localhost:49000/api/customer/{customerId}/transaction/{accType}/lodg/{accNumber}/create"    
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/transaction/{accType}/lodg/{accNumber}/create")
    public Response createLodgementTransaction(@PathParam("customerId") int customerId, 
                                            @PathParam("accType") String accType, 
                                            @PathParam("accNumber") int accNumber, 
                                            String body){
        Gson gson = new Gson();
        Transaction newTransaction = gson.fromJson(body,Transaction.class);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.
                lodgement(customerId, accType, accNumber, newTransaction))).build();
    }    
    
    
    
    //2.10. TO CREATE A TRANSFER BETWEEN OWN ACCOUNTS TRANSACTION-------------------------------------------------------
    //THE METHOD WILL CREATE A MESSAGE IF TRANSFER TRANSACTION WAS SUCCESSFUL OR NOT. IF IT WAS SUCCESSFUL IT WILL RETURN 
    //ACCOUNT BALANCE OF WITHDRAWAL ACCOUNT IN A MESSAGE, AS WELL IT WILL CREATE A TRANSACTIONS FOR BOTH ACCOUNTS
    //TRANSACTION AMOUNT IS SENT IN THE BODY
    //API name:create_transfer_between_accounts_transaction
    // In Postman "http://localhost:49000/api/customer/{customerId}/transaction/{accountTypeOut}/wid/{accNumberOut}/betweenAccounts/{accountTypeIn}/lodg/{accNumberIn}/create"
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/transaction/{accountTypeOut}/wid/{accNumberOut}/betweenAccounts/{accountTypeIn}/lodg/{accNumberIn}/create")
    public Response createTransferBetweenAccountsTransaction(@PathParam("customerId") int customerId, 
                                                            @PathParam("accountTypeOut") String accountTypeOut, 
                                                            @PathParam("accNumberOut") int accNumberOut,
                                                            @PathParam("accountTypeIn") String accountTypeIn, 
                                                            @PathParam("accNumberIn") int accNumberIn,
                                                            String body){
        Gson gson = new Gson();
        Transaction newTransaction = gson.fromJson(body,Transaction.class);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.
                transferBetweenAccounts(customerId, accountTypeOut, accNumberOut, accountTypeIn, accNumberIn, newTransaction))).build();
    }   
    
    
    
    //2.10. TO CREATE A TRANSFER BETWEEN DIFFERENT CUSTOMERS ACCOUNT TRANSACTION----------------------------------------
    //TO CREATE A MESSAGE IF TRANSACTION BETWEEN TWO DIFFERENT CUSTOMERS WAS SUCCESSFUL OR NOT. IF IT WAS SUCCESSFUL 
    //THE METHOD WILL RETURN ACCOUNT BALANCE OF WITHDRAWAL ACCOUNT IN MESSAGE, AS WELL IT WILL CREATE A TRANSACTIONS FOR BOTH CUSTOMERS ACCOUNTS
    //TRANSACTION AMOUNT IS SENT IN THE BODY
    //API name:create_transfer_between_customers_transaction
    // In Postman "http://localhost:49000/api/customer/{customerId}/transaction/{accountTypeOut}/wid/{accNumberOut}/betweenAccounts/{accountTypeIn}/lodg/{accNumberIn}/create"
    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerIdOut}/account/{accountTypeOut}/wid/{accNumberOut}/betweenCustomers/{customerNameIn}/account/{accNumberIn}/create")
    public Response getTransferBetweenCustomersTransaction(@PathParam("customerIdOut") int customerIdOut, 
                                                            @PathParam("accountTypeOut") String accountTypeOut, 
                                                            @PathParam("accNumberOut") int accNumberOut,
                                                            @PathParam("customerNameIn") String customerNameIn, 
                                                            @PathParam("accNumberIn") int accNumberIn,
                                                            String body){
        Gson gson = new Gson();
        Transaction newTransaction = gson.fromJson(body,Transaction.class);
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.
                transferBetweenCustomers(customerIdOut, accountTypeOut, accNumberOut,
                                         customerNameIn, accNumberIn, newTransaction))).build();
    }   
    
    
    //2.11. TO DISPLAY TRANSACTIONS BY ACCOUNT TYPE---------------------------------------------------------------------
    //USED IN CUSTOMER AND ADMIN APP
    //API name:get_transactions
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/{customerId}/getUA/{accounType}/trans")
    public Response getTransactionList(@PathParam("customerId") int customerId, 
                                        @PathParam("accounType") String accounType){
        Gson gson = new Gson();
        return Response.status(Response.Status.CREATED).entity(gson.toJson(customer.getTransactions(customerId, accounType))).build();
    }  
    
    
}
