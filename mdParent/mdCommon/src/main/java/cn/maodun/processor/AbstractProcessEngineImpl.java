package cn.maodun.processor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;

import java.util.List;

/**
 * @author DELL
 * @date 2023/4/11
 */
@Slf4j
public abstract class AbstractProcessEngineImpl implements ProcessEngine {

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public void start(ProcessRequest processRequest, ProcessContext processContext) {
      List<ProcessNameEnum> processors = getProcessors();

      processors.forEach(processNameEnum -> {
          Object bean = applicationContext.getBean(processNameEnum.name());
          if (bean instanceof  Processor){
              Processor processor = (Processor) bean;

              if (!processor.needExecute(processRequest,processContext)){
                  log.info("processor:" + processNameEnum + " skipped");
                  return;
              }

              processor.execute(processRequest,processContext);
          }
      });

    }

    abstract List<ProcessNameEnum> getProcessors() ;
}
