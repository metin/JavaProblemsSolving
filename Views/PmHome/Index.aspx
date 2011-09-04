<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Pm.Master" Inherits="System.Web.Mvc.ViewPage<dynamic>" %>


<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Index
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    
    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">
        <div class="section">

            <h1>Project Management Dashboard</h1>
            <p>Select an action to see deatils</p>
        </div>
    </div>

    <div class="grid_6">
        <div class="section">

             <h1>Manage Projects</h1>
            <p>Add project or create a milestone</p>

            <table class="details">
                <tr>
                    <th style="width:180px;">Projects:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Project.FindAll().Count.ToString(), "Index", "Projects")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Project", "Create", "Projects")%>
                    </td>

                </tr>
                <tr>
                    <th>Milestones:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Milestone.FindAll().Count.ToString(), "Index", "Milestones")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("Define a milestone", "Create", "Milestones")%>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="grid_6">
        <div class="section">

            <h1>Bugs</h1>
            <p></p>

            <table class="details">
                <tr>
                    <th style="width:180px;">All Bugs:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Bug.FindAll().Count.ToString(), "Index", "Bugs")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("Report a bug", "Create", "Bugs")%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">Open:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Bug.FilterAll("Open", "", null).Count.ToString(), "Index", "Bugs", new { status = "Open" }, new { })%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">In Process:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Bug.FilterAll("In Process", "", null).Count.ToString(), "Index", "Bugs", new { status = "In Process" }, new { })%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">Closed:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Bug.FilterAll("Closed", "", null).Count.ToString(), "Index", "Bugs", new { status = "Closed" }, new { })%>
                    </td>
                </tr>

            </table>
        </div>
    </div>
    
    <div class="clear"></div> 

</asp:Content>
