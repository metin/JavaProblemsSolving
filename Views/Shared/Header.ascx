<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<dynamic>" %>

<div class="grid_12">
    <div id="header">
        Human Resources
    </div>
</div>
<div class="clear"></div> 
<div class="grid_12">
    <div style="text-align:right">
        <span style="border:1px solid #f00;background-color:#744;padding:3px;">
            <%: Html.ActionLink("PM Site", "Index", "Projects", new{},  new { style="color:#fefefe"}) %>
        </span>
    </div>
</div>
<div class="clear"></div> 