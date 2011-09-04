<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<IEnumerable<CS631.Data.Bug>>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">

        <div class="section" > 
            <h1>List Of Bugs</h1>
            <p />
            <div>
                <% using (Html.BeginForm(FormMethod.Get))
                   { %>
                        <table class="filter" >
                            <tr>
                                <td class="noborder"> 
                                    Project: <br />    
                                    <%: Html.DropDownList("ProjID", ViewBag.projects as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder"> 
                                    Status: <br />    
                                    <%: Html.DropDownList("status", ViewBag.statees as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder"> 
                                    Type: <br />    
                                    <%: Html.DropDownList("type", ViewBag.types as SelectList, "")%>
                                </td>
                                <td class="noborder">&nbsp;</td>
                                <td class="noborder" style="width:30px;" valign="bottom">
                                    &nbsp;<br />
                                    <input type="submit" value="Filter" class="jqui_button_edit1" style="width:50px;"/>
                                </td>
                            </tr>
                        </table>
                       
                <% } %>
            </div>
            <table width="100%">
                <thead>
                    <tr>
                        <th>Bug NO</th>
                        <th>Project NO</th>
                        <th>Asignee</th>
                        <th>Type</th>
                        <th>Status</th>
                        <th>Date</th>
                        <th>Details</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model)  { %>
                        <tr>
                            <td><%: p.BugID %> </td>
                            <td><%: p.ProjectNO %> </td>
                            <td><%: p.Assignee %> </td>
                            <td><%: p.Type %></td>
                            <td><%: p.Status %></td>
                            <td><%: p.DateReported.ToString("MM/dd/yyyy") %> </td>
                            <td><%: p.Details%> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", new { id = p.BugID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                                <%: Html.ActionLink("Edit", "Edit", new { id = p.BugID }, new { @class = "jqui_button_edit" })%> 
                                <%: Html.ActionLink("Delete", "Delete", new { id = p.BugID }, new { @class = "jqui_button_delete" })%>
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>
        </div>

    </div>
     <div class="clear"></div> 
</asp:Content>
