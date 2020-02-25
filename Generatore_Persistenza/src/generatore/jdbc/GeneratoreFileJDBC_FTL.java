package generatore.jdbc;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import generatore.global.Bean;

public class GeneratoreFileJDBC_FTL {

	private Configuration configuration;

	public GeneratoreFileJDBC_FTL() throws IOException {		
		configuration = new Configuration(Configuration.VERSION_2_3_28);
		
		// Set the root of the class path ("") as the location to find templates
		configuration.setClassLoaderForTemplateLoading(getClass().getClassLoader(), "");
		
		configuration.setDefaultEncoding("UTF-8");
		configuration.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
		configuration.setLogTemplateExceptions(false);
		configuration.setWrapUncheckedExceptions(true);
	}

	public void generateJavaBeanFiles(Bean classSpecification, String inputPath, String nomeFile, File outputDirectory) throws Exception {
		Map<String, Object> freemarkerDataModel = new HashMap<>();
		
		// Get the template to generate Java source files
		//String path ="./resources/Bean.ftl";
		Template template = configuration.getTemplate(inputPath);
		
		//for (ClasseProblema classSpecification : classSpecifications) {
			// Put the classSpecification into the data model.
			// It can  be accessed in the template through ${classSpecification}
			freemarkerDataModel.put("bean", classSpecification);
			
			// The Java source file will be generated in the same directory as the YAML file
			File javaSourceFile = new File(outputDirectory, nomeFile);
			Writer beanFileWriter = new FileWriter(javaSourceFile);
			
			// Generate the Java source file
			template.process(freemarkerDataModel, beanFileWriter);
		//}
	}

	public void generateJavaJDBCFiles(Repository classSpecification,String inputPath, String nomeFile, File outputDirectory)throws Exception {
		Map<String, Object> freemarkerDataModel = new HashMap<>();
		
		// Get the template to generate Java source files
		//String path ="./resources/DB2DAO.ftl";
		Template template = configuration.getTemplate(inputPath);
		//for (ClasseProblema classSpecification : classSpecifications) {
			// Put the classSpecification into the data model.
			// It can  be accessed in the template through ${classSpecification}
			freemarkerDataModel.put("repository", classSpecification);
			// The Java source file will be generated in the same directory as the YAML file
			File javaSourceFile = new File(outputDirectory, nomeFile);
			Writer beanFileWriter = new FileWriter(javaSourceFile);
			
			// Generate the Java source file
			template.process(freemarkerDataModel, beanFileWriter);
	}

	public void generateJavaJDBCTest(List<Repository> repositories, String input, String nomeFile, File outputDirectory)throws Exception {
		Map<String, Object> freemarkerDataModel = new HashMap<>();

		Template template = configuration.getTemplate(input);

		freemarkerDataModel.put("repositories", repositories);

		File javaSourceFile = new File(outputDirectory, nomeFile);
		Writer beanFileWriter = new FileWriter(javaSourceFile);


		template.process(freemarkerDataModel, beanFileWriter);
		
	}

	public void generateJavaFile(String input, String nomeFile, File outputDirectory) throws Exception {
		Map<String, Object> freemarkerDataModel = new HashMap<>();

		Template template = configuration.getTemplate(input);


		File javaSourceFile = new File(outputDirectory, nomeFile);
		Writer beanFileWriter = new FileWriter(javaSourceFile);


		template.process(freemarkerDataModel, beanFileWriter);
	}	
}
