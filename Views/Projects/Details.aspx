<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Pm.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Project>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Details
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">

            <h1>Project Details</h1>
            <p>Details of project that</p>

            <table class="details">
                <tr>
                    <th><%: Html.LabelFor(model => model.ProjName) %></th>
                    <td>
                        <%: Model.ProjName  %>
                    </td>
                </tr>
                <tr>
                    <th> <%: Html.LabelFor(model => model.ProjBudget)%></th>
                    <td>
                        <%: Model.ProjBudget %>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.StartDate )%></th>
                    <td>
                        <%: Model.StartDate %>
                    </td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.EndDate)%></th>
                    <td>
                        <%: Model.EndDate %>
                    </td>
                </tr>
                <tr>
                    <th><label>Members</label></th>
                    <td>
                        <%: Model.Members(CS631.Data.ProjectMember.MembershipStatus.Current).Count<CS631.Data.ProjectMember>() %>
                    </td>
                </tr>
                <tr>
                    <th><label>Milestones</label></th>
                    <td>
                        <%: Model.Milestones().Count<CS631.Data.Milestone>()%>
                    </td>
                </tr>
                
            </table>
        </div>
    </div>
    <div class="clear"></div> 
</asp:Content>

