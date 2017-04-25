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
import {StatusDetailComponent} from "../../../../../../main/webapp/app/entities/status/status-detail.component";
import {StatusService} from "../../../../../../main/webapp/app/entities/status/status.service";
import {Status} from "../../../../../../main/webapp/app/entities/status/status.model";

describe('Component Tests', () => {

	describe('Status Management Detail Component', () => {
		let comp: StatusDetailComponent;
		let fixture: ComponentFixture<StatusDetailComponent>;
		let service: StatusService;

		beforeEach(async(() => {
			TestBed.configureTestingModule({
				declarations: [StatusDetailComponent],
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
					StatusService
				]
			}).overrideComponent(StatusDetailComponent, {
				set: {
					template: ''
				}
			}).compileComponents();
		}));

		beforeEach(() => {
			fixture = TestBed.createComponent(StatusDetailComponent);
			comp = fixture.componentInstance;
			service = fixture.debugElement.injector.get(StatusService);
		});


		describe('OnInit', () => {
			it('Should call load all on init', () => {
				// GIVEN

				spyOn(service, 'find').and.returnValue(Observable.of(new Status(10)));

				// WHEN
				comp.ngOnInit();

				// THEN
				expect(service.find).toHaveBeenCalledWith(123);
				expect(comp.status).toEqual(jasmine.objectContaining({id: 10}));
			});
		});
	});

});
