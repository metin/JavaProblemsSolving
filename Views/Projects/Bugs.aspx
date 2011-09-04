<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Project>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Bugs
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">

            <h1>Bugs</h1>
            <p></p>

            <table width="100%">
                <thead>
                    <tr>
                        <th>Bug NO</th>
                        <th>Asignee</th>
                        <th>Type</th>
                        <th>Date</th>
                        <th>Details</th>
                        <th>Actions</th>    
                    </tr>
                </thead>
                <tbody>
                    <% foreach (var p in Model.Bugs())  { %>
                        <tr>
                            <td><%: p.BugID %> </td>
                            <td><%: p.Assignee %> </td>
                            <td><%: p.Type %></td>
                            <td><%: p.DateReported.ToString("MM/dd/yyyy") %> </td>
                            <td><%: p.Details%> </td>
                            <td class="action_buttons"> 
                                <%: Html.ActionLink("Show", "Details", "Bugs", new { id = p.BugID }, new { @class = "jqui_button_show", style = "padding: 0px;" })%> 
                            </td>
                        </tr>
                    <% } %>
                </tbody>
            </table>



        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>
