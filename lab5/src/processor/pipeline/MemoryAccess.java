package processor.pipeline;

import processor.Processor;
import processor.memorysystem.MainMemory;

public class MemoryAccess {
	Processor containingProcessor;
	EX_MA_LatchType EX_MA_Latch;
	MA_RW_LatchType MA_RW_Latch;
	IF_OF_LatchType IF_OF_Latch;
	
	public MemoryAccess(Processor containingProcessor, EX_MA_LatchType eX_MA_Latch, MA_RW_LatchType mA_RW_Latch,IF_OF_LatchType iF_OF_Latch)
	{
		this.containingProcessor = containingProcessor;
		this.EX_MA_Latch = eX_MA_Latch;
		this.MA_RW_Latch = mA_RW_Latch;
		this.IF_OF_Latch = iF_OF_Latch;
	}
	
	public void performMA()
	{
		//TODO
		MA_RW_Latch.set_stall(EX_MA_Latch.get_stall());
		if(EX_MA_Latch.isMA_enable()&&EX_MA_Latch.get_stall()){
			//System.out.println("MA ");
			int rd=EX_MA_Latch.get_rd();
			int res=EX_MA_Latch.get_res();
			int opcode=EX_MA_Latch.get_opcode();
			if(opcode==22||opcode==23){
				MainMemory mainMemory=containingProcessor.getMainMemory();
				if(opcode==22){
					res=mainMemory.getWord(res);
				}
				if(opcode==23){
					mainMemory.setWord(res, rd);
					containingProcessor.setMainMemory(mainMemory);
				}
			}
			if(opcode>=23&&opcode<29&&res==-1){
				IF_OF_Latch.set_stall(true);
			}
			MA_RW_Latch.set_rd(rd);
			MA_RW_Latch.set_res(res);
			MA_RW_Latch.set_opcode(opcode);
			EX_MA_Latch.setMA_enable(false);
			MA_RW_Latch.setRW_enable(true);
		}
	}

}
