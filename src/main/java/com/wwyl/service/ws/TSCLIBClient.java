package com.wwyl.service.ws;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import com.sun.jna.Library;
import com.sun.jna.Native;
/**
 * 海吉星TSC-TTP-243E Pro接口
 * <p/>
 * Created by hehao on 16-7-15.
 */
@Component
public class TSCLIBClient {  
/**  
* @param args  
*/  
	
	@Value("${tsc.printer_name}")
	String printerName;
	
	public interface TSCLIB_DLL_243E extends Library {
		TSCLIB_DLL_243E INSTANCE = (TSCLIB_DLL_243E) Native.loadLibrary ("TSCLIB", TSCLIB_DLL_243E.class);
        int about ();
        int openport (String pirnterName);
        int closeport ();
        int sendcommand (String printerCommand);
        int setup (String width,String height,String speed,String density,String sensor,String vertical,String offset);
        int downloadpcx (String filename,String image_name);
        int barcode (String x,String y,String type,String height,String readable,String rotation,String narrow,String wide,String code);
        int printerfont (String x,String y,String fonttype,String rotation,String xmul,String ymul,String text);
        int clearbuffer ();
        int printlabel (String set, String copy);
        int formfeed ();
        int nobackfeed ();
        int windowsfont (int x, int y, int fontheight, int rotation, int fontstyle, int fontunderline, String szFaceName, String content);
    }
      
    //打印方法，传入需要打印的条码参数  
    public void printLabel(String datetime,String customerName,String productName,String amount,String weight,String indexNum) {   
    		System.setProperty("jna.encoding", "GBK");
    		TSCLIB_DLL_243E.INSTANCE.openport(printerName);// 打开 打印机 端口.
    		TSCLIB_DLL_243E.INSTANCE.setup("40", "35", "3", "5", "0", "5", "0");
    		TSCLIB_DLL_243E.INSTANCE.clearbuffer();// 清除缓冲信息
    		TSCLIB_DLL_243E.INSTANCE.sendcommand("GAP 1 mm,0");// 设置 打印的方向.
    		TSCLIB_DLL_243E.INSTANCE.sendcommand("DIRECTION 1");// 设置 打印的方向.
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(20, 50, 20, 0, 3, 0, "宋体", datetime);
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(20, 100, 30, 0, 3, 0, "宋体", "客户"+" "+customerName + "  " +"农通");
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(20, 150, 30, 0, 3, 0, "宋体", "品类"+" "+productName);
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(20, 200, 20, 0, 3, 0, "宋体", "件数"+" "+amount);
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(150, 200, 20, 0, 3, 0, "宋体", " 重量 "+" "+weight+" Kg");
    		TSCLIB_DLL_243E.INSTANCE.windowsfont(130, 300, 20, 0, 3, 0, "宋体", indexNum);
    		TSCLIB_DLL_243E.INSTANCE.printlabel("1", "1");
    		TSCLIB_DLL_243E.INSTANCE.clearbuffer();// 清除缓冲信息
    		TSCLIB_DLL_243E.INSTANCE.closeport();
    }   
}
