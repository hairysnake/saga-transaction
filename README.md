# saga-transaction
Simple project for visualization how SAGA pattern can be implemented using annotation and AOP in SpringBoot.

# Preconditions

* A method that should be executed in one transation should be annotated with @SagaTransaction
* Inside the SagaTransaction method one should execute external services (beans) to communicate with other microservices
* Because it uses Spring AOP it won't work with methods inside the same bean
* Methods communicating with other microservices (in transaction) should be annotated with @SagaMethod("rollbackMethodName")
* The roolbackMethodName is the name of the method which will be executed in case of a transaction rollback
* Rollback method must take as incoming parameter value returned by method annotated with @SagaMethod


