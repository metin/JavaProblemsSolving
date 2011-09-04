<%@ Page Title="" Language="C#" MasterPageFile="~/Views/Shared/Site.Master" Inherits="System.Web.Mvc.ViewPage<CS631.Data.Employee>" %>

<asp:Content ID="Content1" ContentPlaceHolderID="TitleContent" runat="server">
    Salary
</asp:Content>

<asp:Content ID="Content2" ContentPlaceHolderID="MainContent" runat="server">
    <script type="text/javascript">
        $(function () {
            $("#SalaryStartDate").datepicker({ showOn: 'both', buttonImage: "/Public/Images/calendar.gif", buttonImageOnly: true });
            $("#tabs").tabs();
        });
	</script>

    <% Html.RenderPartial("SubMenu"); %>

    <div class="grid_2 nopadding">
        <% Html.RenderPartial("LeftMenu", Model, new ViewDataDictionary(Model)); %>
    </div>

    <div class="grid_10">
        <div class="section">
            <h1>Employe salary</h1>
            <p>Update employee salay, see salary history</p>

            <div id="tabs">
                <ul>
                    <li><a href="#tabs-1">Current</a></li>
                    <li><a href="#tabs-2">Past</a></li>
                </ul>
                <div id="tabs-1">
                    <% var curSalary = Model.CurrentSalary(); %>
                    <table class="details">
                        <thead>
                            <tr style="border: 2px solid #aedecc;">
                                <th> <label>Current salary is </label> </th>
                                <td align="left"> <%: String.Format("${0:F}", curSalary.AnnualSalary) %> </td>
                        
                            </tr>
                        </thead>
                    </table>

                    <h1>Update Salary</h1>
                    <p> </p>
                    <% using (Html.BeginForm("Salary", "Employees", FormMethod.Post)) { %>
                        <%: Html.ValidationSummary(true) %>
                        <%: Html.HiddenFor(model => model.EmpID)%>
                        <table class="details">
                            <tr>
                                <th><%: Html.Label("Salary", "Salary") %></th>
                                <td><%: Html.Editor("AnnualSalary") %></td>
                            </tr>
                            <tr>
                                <th><%: Html.Label("Salary", "Salary Date")%></th>
                                <td><%: Html.Editor("SalaryStartDate")%></td>
                            </tr>
                        </table>
                        <div class="clear"></div> 
                        <div>
                            <input type="submit" value="Save" class="action_button"/>
                        </div>
                        <div class="clear"></div> 
                    <% } %>
                </div>
                <div id="tabs-2">
                    <h1>History</h1>
                    <p>Previous salaries </p>
                    <table width="100%">
                        <thead>
                            <tr>
                                <th> Date </th>
                                <th> Annual Salary </th>
                            </tr>
                        </thead>
                        <tbody>
                            <% foreach (var d in Model.SalaryHistory())
                                { %>
                                <tr>
                                    <td><%: d.SalaryStartDate.ToString("MM/dd/yyyy")%> </td>
                                    <td><%: String.Format("${0:F}", d.AnnualSalary) %> </td>
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
