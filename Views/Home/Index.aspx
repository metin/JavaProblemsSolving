<%@ Page Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Home Page
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    
    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_12">

            <div class="section">

            <h1>Company Dashboard</h1>
            <p>Select an action to see deatils</p>
            </div>
    </div>

    <div class="grid_6">

            <div class="section">

            <h1>Company Management</h1>
            <p>Divisons, departments management</p>

            <table class="details">
                <tr>
                    <th style="width:180px;">Divisions:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Division.FindAll().Count<CS631.Data.Division>().ToString(), "Index", "Divisions")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Division", "Create", "Divisions")%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">Departments:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Department.FindAll().Count<CS631.Data.Department>().ToString(), "Index", "Departments")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Department", "Create", "Departments")%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">Offices:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Office.FindAll().Count<CS631.Data.Office>().ToString(), "Index", "Offices")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Office", "Create", "Offices")%>
                    </td>
                </tr>
                <tr>
                    <th style="width:180px;">Buildings:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Building.FindAll().Count<CS631.Data.Building>().ToString(), "Index", "Buildings")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Building", "Create", "Buildings")%>
                    </td>
                </tr>
            </table>
        </div>
    </div>

    <div class="grid_6">

            <div class="section">

            <h1>Payroll Management</h1>
            <p>Employee, payroll managements</p>

            <table class="details">
                <tr>
                    <th style="width:180px;">Employees:</th>
                    <td>
                        <%: Html.ActionLink(CS631.Data.Employee.FindAll().Count<CS631.Data.Employee>().ToString(), "Index", "Employees")%>
                        &nbsp;&nbsp;|&nbsp;
                        <%: Html.ActionLink("New Employee", "Create", "Employees")%>
                    </td>

                </tr>
                <tr>
                    <th>Payroll</th>
                    <td>
                        <%: Html.ActionLink("Chosee an employee", "Index", "Employees")%>
                    </td>

                </tr>

                
            </table>
        </div>
    </div>

    <div class="clear"></div> 

</asp:Content>
