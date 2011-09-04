<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<dynamic>" %>

<% string curAction = ViewContext.Controller.ValueProvider.GetValue("action").RawValue as string; %> 
<% string curController =  ViewContext.Controller.ValueProvider.GetValue("controller").RawValue as string; %> 


<div id="menu_container">
    <ul id="menu">
        <li class="<%: curController=="Home" ? "current" : "" %>">
            <%: Html.ActionLink("Home", "Index", "Home") %>
        </li>
        <li class="<%: curController=="Divisions" ? "current" : "" %>">
            <%: Html.ActionLink("Divisions", "Index", "Divisions")%>
        </li>    

        <li class="<%: curController=="Departments" ? "current" : "" %>">
            <%: Html.ActionLink("Departments", "Index", "Departments")%>
        </li>    

        <li class="<%: (curController=="Buildings") ? "current" : "" %>">
            <%: Html.ActionLink("Buildings", "Index", "Buildings")%>
        </li>
        <!--
        <li class="<%: (curController=="Rooms") ? "current" : "" %>">
            <%: Html.ActionLink("Rooms", "Index", "Rooms")%>
        </li>
        -->

        <li class="<%: (curController=="Offices") ? "current" : "" %>">
            <%: Html.ActionLink("Offices", "Index", "Offices")%>
        </li>

        <li class="<%: curController=="Employees" ? "current" : "" %>">
            <%: Html.ActionLink("Employees", "Index", "Employees") %>
        </li>

    </ul>
</div>


