

package draw1; 

import java.util.*; 
import scribble3.Tool; 

public class ToolKit {

  public ToolKit() {
  }
  
  /**
    Add a new tool to the tool kit. 
    Return the index of the new tool. 
   */
  public int addTool(Tool tool) {
    if (tool != null) {
      tools.add(tool);
      return (tools.size() - 1); 
    }
    return -1; 
  }

  public int getToolCount() {
    return tools.size(); 
  }
  
  public Tool getTool(int i) { 
    if (i >= 0 &&
	i < tools.size()) { 
      return (Tool) tools.get(i); 
    }
    return null;
  }

  public Tool findTool(String name) { 
    if (name != null) {
      for (int i = 0; i < tools.size(); i++) { 
	Tool tool = (Tool) tools.get(i); 
	if (name.equals(tool.getName())) {
	  return tool;
	}
      }
    }
    return null;
  }

  public void setSelectedTool(int i) { 
    Tool tool = getTool(i); 
    if (tool != null) { 
      selectedTool = tool; 
    }
  }

  public Tool setSelectedTool(String name) { 
    Tool tool = findTool(name); 
    if (tool != null) { 
      selectedTool = tool; 
    }
    return tool;
  }

  public void setSelectedTool(Tool tool) { 
    selectedTool = tool; 
  }

  public Tool getSelectedTool() { 
    return selectedTool;
  }

  protected List tools = new ArrayList(16); 
  protected Tool selectedTool = null; 

}
