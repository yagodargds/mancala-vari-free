package rf.yagodar.manqala.free.logic.exception.ai;

//TODO DOC

import java.io.IOException;

@SuppressWarnings("serial")
public class CreateAIFailed extends IOException{
	
	public CreateAIFailed()
	{}
	
	public CreateAIFailed(String gripe)
	{
		super(gripe);
	}
}
