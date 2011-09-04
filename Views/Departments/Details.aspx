<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Department>" %>

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
            <h1>Show Department</h1>
            <p>Details of department</p>
  

            <table class="details">
                <tr>
                    <th> <label>Departmen No</label> </th>
                    <td>
                        <%: Model.DepartmentNo %>
                    </td>
                </tr>
                <tr>
                    <th> <label>Departmen Name</label> </th>
                    <td>
                        <%: Model.DeptName%>
                    </td>
                </tr>
                <tr>
                    <th> <label>Department Head</label> </th>
                    <td>
                        <%: Model.DepartmentHead %>
                    </td>
                </tr>
                <tr>
                    <th> <label> Division Name</label> </th>
                    <td>
                        <%: Model.DivisionName %>
                    </td>
                </tr>
            </table>


        </div>
                
    </div>
    
    <div class="clear"></div> 

</asp:Content>

