import {ComponentFixture, TestBed, async} from "@angular/core/testing";
import {MockBackend} from "@angular/http/testing";
import {Http, BaseRequestOptions} from "@angular/http";
import {OnInit} from "@angular/core";
import {DatePipe} from "@angular/common";
import {ActivatedRoute} from "@angular/router";
import {Observable} from "rxjs/Rx";
import {DateUtils, DataUtils, JhiLanguageService} from "ng-jhipster";
import {MockLanguageService} from "../../../helpers/mock-language.service";
import {MockActivatedRoute} from "../../../helpers/mock-route.service";
import {InactiveReasonDetailComponent} from "../../../../../../main/webapp/app/entities/inactive-reason/inactive-reason-detail.component";
import {InactiveReasonService} from "../../../../../../main/webapp/app/entities/inactive-reason/inactive-reason.service";
import {InactiveReason} from "../../../../../../main/webapp/app/entities/inactive-reason/inactive-reason.model";

describe('Component Tests', () => {

	describe('InactiveReason Management Detail Component', () => {
		let comp: InactiveReasonDetailComponent;
		let fixture: ComponentFixture<InactiveReasonDetailComponent>;
		let service: InactiveReasonService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [InactiveReasonDetailComponent],
				providers: [
					MockBackend,
					BaseRequestOptions,
					DateUtils,
					DataUtils,
					DatePipe,
					{
						provide: ActivatedRoute,
						useValue: new MockActivatedRoute({id: 123})
					},
					{
						provide: Http,
						useFactory: (backendInstance: MockBackend, defaultOptions: BaseRequestOptions) => {
							return new Http(backendInstance, defaultOptions);
						},
						deps: [MockBackend, BaseRequestOptions]
					},
					{
						provide: JhiLanguageService,
						useClass: MockLanguageService
					},
					InactiveReasonService
				]
			}).overrideComponent(InactiveReasonDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(InactiveReasonDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(InactiveReasonService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new InactiveReason(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.inactiveReason).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
