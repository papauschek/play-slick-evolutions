Slick 2.1 Code Generator and Play Framework 2.3 Evolutions
==========================

This sample project shows how to integrate the Slick 2.1 code generator with the Play Framework Evolutions (database migrations) for Play Framework 2.3.
The necessary Slick code for database access is automatically generated, so that we're ready to query our database any time we change the database structure.

DbGen Play Module
-----------------

The sample prototype project uses a Play sub-module called "dbgen" for code generation.
The "dbgen" module consists of just one class (PlaySlickCodeGenerator.scala) and takes
care of applying evolutions from the main project into an in-memory database.
The main method starts a fake Play application, invokes the Evolutions plugin, and then calls the Slick code generator.

### Known Limitations

The SQL in the Evolutions files has to be compatible with both your production database and the H2 in-memory database. H2 can be configured to be compatible with most databases. This example uses MySQL, hence it activates the MySQL compatibility mode of H2.

### More Information
For more information see http://blog.papauschek.com/2013/12/slick-2-0-code-generator-play-framework-evolutions/


