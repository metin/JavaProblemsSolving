<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/PM.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Project>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Members
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <script type="text/javascript">
        $(function () {
            $("#tabs").tabs();
        });
	</script>
    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">

            <h1>Project Members</h1>
            <p></p>
            <div id="tabs">
                <ul>
                    <li><a href="#tabs-1">Current Members</a></li>
                    <li><a href="#tabs-2">Add New Member</a></li>
                    <li><a href="#tabs-3">Previous Members</a></li>
                </ul>
                <div  id="tabs-1">
                    <table width="100%">
                        <thead>
                            <tr>
                                <th>Employee</th>
                                <th>Since</th>
                                <th>Role</th>
                                <th>Total</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% foreach (var p in Model.Members(CS631.Data.ProjectMember.MembershipStatus.Current))
                               { %>
                                <tr>
                                    <td><%: p.EmpName%> </td>
                                    <td><%: p.StartDate %> </td>
                                    <td><%: p.Role %> </td>
                                    <td><%: String.Format("{0:F} hours", p.DaysWorked) %> </td>
                                    <td class="action_buttons"> 
                                        <%: Html.ActionLink("Delete", "Delete", "ProjectMemberships", new { id = p.EmpProj }, new { @class = "jqui_button_delete", style = "padding: 0px;" })%> 
                                        <%: Html.ActionLink("Finish", "Finish", "ProjectMemberships", new { id = p.EmpProj }, new { @class = "jqui_button_edit", style = "padding: 0px;" })%> 
                                    </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>
                <div id="tabs-2">
                    <% Html.RenderPartial("NewMember", new CS631.Data.ProjectMember() ); %>
                </div>

                <div  id="tabs-3">
                    <table width="100%">
                        <thead>
                            <tr>
                                <th>Employee</th>
                                <th>Since</th>
                                <th>Role</th>
                                <th>Total</th>
                            </tr>
                        </thead>
                        <tbody>
                            <% foreach (var p in Model.Members(CS631.Data.ProjectMember.MembershipStatus.Ended))
                               { %>
                                <tr>
                                    <td><%: p.EmpName %> </td>
                                    <td><%: p.StartDate %> </td>
                                    <td><%: p.Role %> </td>
                                    <td> <%: String.Format("{0:F} hours", p.DaysWorked)%> </td>
                                </tr>
                            <% } %>
                        </tbody>
                    </table>
                </div>

            </div>


        </div>
    </div>
    <div class="clear"></div> 

</asp:Content>
