<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Employee>" %>

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

            <h1>Show Employee</h1>
            <p>Details of employee</p>
            
            <table class="details">
                <tr>
                    <th><label>Employee NO:</label></th>
                    <td><%: Model.EmployeeNO %></td>
                </tr>
                <tr>
                    <th><label>Fist Name</label>:</th>
                    <td><%: Model.EmpFName %></td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpMI)%>:</th>
                    <td><%: Model.EmpMI%></td>
                </tr>
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpLName) %>:</th>
                    <td><%: Model.EmpLName %></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpTitle)%>:</th>
                    <td><%: Model.EmpTitle%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpBuilding)%>:</th>
                    <td><%: Model.EmpBuilding%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpOffice)%>:</th>
                    <td><%: Model.EmpOffice%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpPhone)%>:</th>
                    <td><%: Model.EmpPhone%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpDept)%>:</th>
                    <td><%: Model.EmpDept%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpDiv)%>:</th>
                    <td><%: Model.EmpDiv%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.EmpType)%>:</th>
                    <td><%: Model.EmpType%></td>
                </tr>                
                <tr>
                    <th><%: Html.LabelFor(model => model.HourRate)%>:</th>
                    <td><%: Model.HourRate%></td>
                </tr>               

            </table>
        </div>
    </div>
    <div class="clear"></div> 
</asp:Content>
