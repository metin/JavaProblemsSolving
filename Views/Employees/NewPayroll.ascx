<%@ Control Language="C#" Inherits="System.Web.Mvc.ViewUserControl<CS631.Data.Payroll>" %>

<% var empl = ViewBag.employee as CS631.Data.Employee; %>

<% using (Html.BeginForm("Payroll", "Employees", FormMethod.Post)) { %>
    <%: Html.ValidationSummary(true) %>


    <%: Html.Hidden("EmpID", empl.EmpID)%>

    <table class="details">

        <tr>
            <th><%: Html.LabelFor(model => model.PayDate) %></th>
            <td>
                <%: Html.EditorFor(model => model.PayDate)%>
                <%: Html.ValidationMessageFor(model => model.PayDate)%>
            </td>
        </tr>

        <tr>
            <th><%: Html.LabelFor(model => model.MonthHours) %></th>
            <td>
                <%: Html.EditorFor(model => model.MonthHours)%>
                <%: Html.ValidationMessageFor(model => model.MonthHours)%>
            </td>
        </tr>
        <tr>
            <th><%: Html.LabelFor(model => model.MonthSalary) %></th>
            <td>
                <%: Html.EditorFor(model => model.MonthSalary)%>
                <%: Html.ValidationMessageFor(model => model.MonthSalary)%>
            </td>
        </tr>

        <tr>
            <th><%: Html.LabelFor(model => model.FedTax)%></th>
            <td>
                <%: Html.EditorFor(model => model.FedTax)%>
                <%: Html.ValidationMessageFor(model => model.FedTax)%>
            </td>
        </tr>


        <tr>
            <th><%: Html.LabelFor(model => model.StateTax)%></th>
            <td>
                <%: Html.EditorFor(model => model.StateTax)%>
                <%: Html.ValidationMessageFor(model => model.StateTax)%>
            </td>
        </tr>

        <tr>
            <th><%: Html.LabelFor(model => model.OtherTax)%></th>
            <td>
                <%: Html.EditorFor(model => model.OtherTax)%>
                <%: Html.ValidationMessageFor(model => model.OtherTax)%>
            </td>
        </tr>

        <tr>
            <th><%: Html.LabelFor(model => model.NetPay)%></th>
            <td>
                <%: Html.EditorFor(model => model.NetPay)%>
                <%: Html.ValidationMessageFor(model => model.NetPay)%>
            </td>
        </tr>

    </table>

    <div class="clear"></div> 
    <div>
        <input type="submit" value="Save" class="action_button"/>
    </div>
    <div class="clear"></div> 


<% } %>

