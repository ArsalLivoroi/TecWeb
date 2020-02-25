package generatore.dao;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;
import generatore.global.Bean;
import generatore.problema.MetodoFind;

public class GeneratoreFileDAO_FTL {

	private Configuration configuration;

	public GeneratoreFileDAO_FTL() throws IOException {		
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

	public void generateJavaDB2DAOFiles(DB2DAO classSpecification,String inputPath, String nomeFile, File outputDirectory)throws Exception {
		Map<String, Object> freemarkerDataModel = new HashMap<>();
		
		// Get the template to generate Java source files
		//String path ="./resources/DB2DAO.ftl";
		Template template = configuration.getTemplate(inputPath);
		//for (ClasseProblema classSpecification : classSpecifications) {
			// Put the classSpecification into the data model.
			// It can  be accessed in the template through ${classSpecification}
			freemarkerDataModel.put("db2dao", classSpecification);
			// The Java source file will be generated in the same directory as the YAML file
			File javaSourceFile = new File(outputDirectory, nomeFile);
			Writer beanFileWriter = new FileWriter(javaSourceFile);
			
			// Generate the Java source file
			template.process(freemarkerDataModel, beanFileWriter);
	}

	public void generateJavaDAOFactory(List<DB2DAO> daos, String inputPath, String nomeFile, File outputDirectory) throws Exception{

		Map<String, Object> freemarkerDataModel = new HashMap<>();

		Template template = configuration.getTemplate(inputPath);

		freemarkerDataModel.put("daos", daos);

		File javaSourceFile = new File(outputDirectory, nomeFile);
		Writer beanFileWriter = new FileWriter(javaSourceFile);


		template.process(freemarkerDataModel, beanFileWriter);
	}

	public void generateJavaProxyFiles(DB2DAO source, Set<MetodoFind<DB2DAO>> set, String inputPath, String nomeFile,
			File outputDirectory) throws Exception {
		
		Map<String, Object> freemarkerDataModel = new HashMap<>();

		Template template = configuration.getTemplate(inputPath);

		freemarkerDataModel.put("source", source);
		freemarkerDataModel.put("metodiFind", set);
		

		File javaSourceFile = new File(outputDirectory, nomeFile);
		Writer beanFileWriter = new FileWriter(javaSourceFile);


		template.process(freemarkerDataModel, beanFileWriter);
		
	}	
}
