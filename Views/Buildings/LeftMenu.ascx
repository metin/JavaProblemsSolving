<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<dynamic>" %>

<% string curAction = ViewContext.Controller.ValueProvider.GetValue("action").RawValue as string; %> 
<% string curController =  ViewContext.Controller.ValueProvider.GetValue("controller").RawValue as string; %> 

<% var b = ViewData.Model as CS631.Data.Building; %>

<div id="left_menu">
    <ul id="left_list">
        <li class="<%: curAction=="Details" ? "current" : "" %>">
            <%: Html.ActionLink("Show", "Details", new { id = b.BuildingID })%>
        </li>
        <li class="<%: curAction=="Edit" ? "current" : "" %>">
            <%: Html.ActionLink("Edit", "Edit", new { id = b.BuildingID })%>
        </li>
        <!--
        <li class="<%: curAction=="Rooms" ? "current" : "" %>">
            <%: Html.ActionLink("Rooms", "Rooms", new { id = b.BuildingID }, new { })%>
        </li>   
        -->

        <li class="<%: curAction=="Offices" ? "current" : "" %>">
            <%: Html.ActionLink("Offices", "Offices", new { id = b.BuildingID }, new { })%>
        </li>   

     </ul>
</div>